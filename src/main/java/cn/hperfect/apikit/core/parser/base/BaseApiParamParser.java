package cn.hperfect.apikit.core.parser.base;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.cons.PrimitiveType;
import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.core.model.type.*;
import cn.hperfect.apikit.core.parser.ApiKitParserFactory;
import cn.hperfect.apikit.core.parser.IApiParamParser;
import cn.hperfect.apikit.core.parser.ParseTypeContext;
import cn.hperfect.apikit.utils.PsiAnnotationUtils;
import cn.hperfect.apikit.utils.PsiDocCommentUtils;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UParameter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseApiParamParser implements IApiParamParser {


    /**
     * 解析query 参数
     *
     * @param method        需要用到方法中的doc
     * @param parameterList
     * @return
     */
    public List<BaseApiPropNode> parseQueryParam(UMethod method, List<UParameter> parameterList) {
        List<BaseApiPropNode> params = new ArrayList<>();
        for (UParameter parameter : parameterList) {
            //判断是否是全局排除
            if (queryParamIsFiltered(parameter)) {
                continue;
            }
            //解析type
            params.add(parseNodeByParam(method, parameter));
        }

        return params;
    }

    /**
     * 解析api 描述
     * 1. 注解
     * 2. 注释 @param xxx xxx
     *
     * @param docComment
     * @param annoApiParam
     * @return
     */
    private String parseApiParamDesc(UParameter parameter, PsiDocComment docComment, UAnnotation annoApiParam) {
        String desc = "";

        if (annoApiParam != null) {
            desc = UAnnotationUtils.findStr(annoApiParam, "value");
        }
        if (StrUtil.isBlank(desc)) {
            //从注释中获取参数描述
            List<PsiDocTag> param = Arrays.stream(docComment.getTags()).filter(i -> i.getName().equals("param")).collect(Collectors.toList());
            PsiDocTag tag = CollUtil.findOne(param, i -> StrUtil.contains(i.getText(), parameter.getName()));
            if (tag != null) {
                String text = tag.getText();
                text = StrUtil.removePrefix(text, "@param");
                desc = StrUtil.removePrefix(StrUtil.trim(text), parameter.getName());
                desc = StrUtil.removeSuffix(desc, "*");
                desc = StrUtil.trim(desc);
                desc = StrUtil.removeSuffix(desc, "\n");
            }
        }
        return desc;
    }

    /**
     * 通过参数解析node
     *
     * @param method
     * @param parameter
     * @return
     */
    private BaseApiPropNode parseNodeByParam(UMethod method, UParameter parameter) {
        List<UAnnotation> uAnnotations = parameter.getUAnnotations();
        UAnnotation requestParam = UAnnotationUtils.getAnnotation(uAnnotations, AnnotationCons.SPRING.REQUEST_PARAM);
        PsiType type = parameter.getType();
        BaseApiPropNode apiParam = parseNodeByType(type);
        UAnnotation annoApiParam = UAnnotationUtils.getAnnotation(parameter, AnnotationCons.SWAGGER.API_PARAM);
        // 额外设置
        apiParam.setName(parseApiParamName(parameter, requestParam, annoApiParam));
        apiParam.setDesc(parseApiParamDesc(parameter, method.getDocComment(), annoApiParam));
        if (requestParam != null) {
            //required
            //apiParam.setRequired();
            apiParam.setDefaultValue(UAnnotationUtils.findStr(requestParam, "defaultValue"));
        }
        //swagger 覆盖参数设置
        if (annoApiParam != null) {
            apiParam.setDefaultValue(UAnnotationUtils.findStr(annoApiParam, "defaultValue"));
            apiParam.setExample(UAnnotationUtils.findStr(annoApiParam, "example"));
            //apiParam.setRequired();
        }


        return apiParam;
    }

    public BaseApiPropNode parseNodeByType(PsiType type) {
        return parseNodeByType(new ParseTypeContext(), type);
    }

    /**
     * 解析类型
     *
     * @param context 存储已解析的class
     * @param type
     * @return
     */
    public BaseApiPropNode parseNodeByType(ParseTypeContext context, PsiType type) {
        //解析type
        if (type instanceof PsiPrimitiveType) {
            //基本类型
            PsiPrimitiveType psiPrimitiveType = (PsiPrimitiveType) type;
            return new ApiPrimitivePropNode(PrimitiveType.matchType(psiPrimitiveType.getName()));
        }
        if (type instanceof PsiWildcardType) {
            //? 类型,当object处理
            return new ApiObjectPropNode();
        }

        if (type instanceof PsiClassType.Stub) {
            //解析class
            PsiClass psiClass = ((PsiClassType.Stub) type).resolve();
            //是否是基本类型
            assert psiClass != null;
            PrimitiveType primitiveType = PrimitiveType.matchType(Objects.requireNonNull(psiClass.getQualifiedName()));
            if (primitiveType != null) {
                return new ApiPrimitivePropNode(primitiveType);
            }
            if (psiClass.isEnum()) {
                PsiField[] allFields = psiClass.getAllFields();
                List<PsiField> fieldList = Arrays.stream(allFields)
                        .filter(PsiEnumConstant.class::isInstance)
                        .collect(Collectors.toList());
                //枚举常量
                List<ApiEnumPropNode.EnumValue> values = fieldList
                        .stream()
                        .map(i -> new ApiEnumPropNode.EnumValue(i.getName(), PsiDocCommentUtils.parseDesc(i.getDocComment())))
                        .collect(Collectors.toList());
                return new ApiEnumPropNode(values);
            }
            //map 类型
            if (psiClass.isInheritor(ApiKitParserFactory.getMapPsiClass(), true)) {
                return new ApiMapPropNode();
            }
            // list类型
            if (psiClass.isInheritor(ApiKitParserFactory.getIterablePsiClass(), true)) {

                PsiClassType classType = (PsiClassType) type;
                //list(List<String>)
                ApiListPropNode apiListPropNode = new ApiListPropNode();

                //获取list 的泛型
                Collection<PsiType> types = classType
                        .resolveGenerics()
                        .getSubstitutor()
                        .getSubstitutionMap()
                        .values();
                //解析出泛型(list 只有一个泛型),设置子属性值
                if (CollUtil.isNotEmpty(types)) {
                    PsiType psiType = types.iterator().next();
                    BaseApiPropNode node = parseNodeByType(context, psiType);
                    apiListPropNode.setParamModelList(ListUtil.toList(node));
                }

                return apiListPropNode;
            }
            //判断是否是file 类型
            if (this.psiClassIsFileType(psiClass)) {
                return new ApiFilePropNode();
            }

            PsiClassType.ClassResolveResult classResolveResult = ((PsiClassReferenceType) type).resolveGenerics();
            PsiSubstitutor substitutor = classResolveResult.getSubstitutor();
            //class 类型
            return parseApiObjectPropNode(context, psiClass, substitutor);
        }


        if (type instanceof PsiArrayType) {
            ApiListPropNode apiListPropNode = new ApiListPropNode();
            //todo
            return apiListPropNode;
        }
        //object 类型处理
        throw new RuntimeException(String.format("类型解析出错:%s,%s", type.toString(), type.getClass().getName()));

    }


    /**
     * 解析对象属性
     *
     * @param context
     * @param psiClass
     * @param substitutor 泛型指示器,解析属性的时候可以通过泛型指示器拿到真实类型
     * @return
     */
    private ApiObjectPropNode parseApiObjectPropNode(ParseTypeContext context, PsiClass psiClass, PsiSubstitutor substitutor) {
        String key = psiClass.getQualifiedName();
        ApiObjectPropNode apiObjectPropNode = context.getClassNode(key);
        if (apiObjectPropNode != null) {
            return new ApiRefPropNode(apiObjectPropNode.getName());
        }

        apiObjectPropNode = new ApiObjectPropNode();
        context.putClassNode(key, apiObjectPropNode);

        PsiField[] allFields = psiClass.getAllFields();
        List<BaseApiPropNode> nodes = new ArrayList<>();
        for (PsiField field : allFields) {
            //忽略final类型
            if (field.getModifierList() != null && field.getModifierList().hasModifierProperty("final")) {
                continue;
            }
            PsiDocComment doc = field.getDocComment();
            //@ignore
            if (PsiDocCommentUtils.hasTag(doc, "ignore")) {
                continue;
            }
            //todo 排除指定的属性

            PsiType psiType = substitutor.substitute(field.getType());
            //解析类型，如果类型是对象，会递归回来，需要防止死循环递归
            BaseApiPropNode baseApiPropNode = parseNodeByType(context,psiType);
            baseApiPropNode.setMockExpr(PsiDocCommentUtils.getJavaDocTagValue(doc, "mock"));
            baseApiPropNode.setDeprecated(field.getAnnotation(AnnotationCons.Deprecated) != null);
            //设置属性信息
            baseApiPropNode.setName(field.getName());
            String desc = PsiDocCommentUtils.parseDesc(doc);
            if (StrUtil.isNotBlank(desc)) {
                baseApiPropNode.setDesc(desc);
            }
            //swagger 注解兼容
            PsiAnnotation annotation = field.getAnnotation(AnnotationCons.SWAGGER.API_MODEL_PROPERTY);
            if (annotation != null) {
                Boolean hidden = PsiAnnotationUtils.getBooleanVal(annotation, "hidden");
                if (hidden != null && hidden == true) {
                    continue;
                }
                Boolean required = PsiAnnotationUtils.getBooleanVal(annotation, "required");
                if (required != null) {
                    baseApiPropNode.setRequired(required);
                }
                //hidden
                String name = PsiAnnotationUtils.getStrValueByName(annotation, "name");
                if (StrUtil.isNotBlank(name)) {
                    baseApiPropNode.setName(name);
                }
                String value = PsiAnnotationUtils.getStrValueByName(annotation, "value");
                if (StrUtil.isNotBlank(value)) {
                    baseApiPropNode.setDesc(value);
                }
                String example = PsiAnnotationUtils.getStrValueByName(annotation, "example");
                if (StrUtil.isNotBlank(example)) {
                    baseApiPropNode.setExample(example);
                }
            }

            nodes.add(baseApiPropNode);
        }
        apiObjectPropNode.setParamModelList(nodes);
        return apiObjectPropNode;
    }

    /**
     * 解析body参数
     *
     * @param parameterList
     * @return
     */
    public BaseApiPropNode parseBodyParam(List<UParameter> parameterList) {
        for (UParameter parameter : parameterList) {
            List<UAnnotation> uAnnotations = parameter.getUAnnotations();
            UAnnotation annotation = CollUtil.findOne(uAnnotations, i -> StrUtil.equals(i.getQualifiedName(), AnnotationCons.SPRING.REQUEST_BODY));
            if (annotation != null) {
                //request body 解析
                PsiType type = parameter.getType();
                //解析类型
                BaseApiPropNode apiObjectType = parseNodeByType(parameter.getType());
                //解析name?
                //todo
                //对象属性解析器

                return apiObjectType;

            }
        }
        return null;
    }


    /**
     * 判断该参数作为query参数是否被过滤
     * 1. 排除spring 内置的一些类型
     *
     * @param parameter
     * @return
     */
    protected abstract boolean queryParamIsFiltered(UParameter parameter);

    /**
     * 从参数中获取api名称
     *
     * @param parameter
     * @param annotation
     * @param annoApiParam
     * @return
     */
    protected abstract String parseApiParamName(UParameter parameter, UAnnotation annotation, UAnnotation annoApiParam);

    /**
     * 判断类型是否为 文件类型
     *
     * @param psiClass
     * @return
     */
    protected abstract boolean psiClassIsFileType(PsiClass psiClass);
}
