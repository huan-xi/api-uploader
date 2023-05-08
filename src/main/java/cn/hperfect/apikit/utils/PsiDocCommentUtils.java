package cn.hperfect.apikit.utils;

import cn.hutool.core.util.StrUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;

import java.util.Optional;

public class PsiDocCommentUtils {

    /**
     * 获取指定tag的值
     *
     * @param docComment
     * @param tagName
     * @return
     */
    public static String getJavaDocTagValue(PsiDocComment docComment, String tagName) {
        if (docComment != null) {
            PsiDocTag[] tags = docComment.getTags();
            return Optional.ofNullable(
                            ArrayUtil.firstMatch(i -> StrUtil.equals(i.getName(), tagName), tags))
                    .map(PsiDocTag::getName)
                    .orElse(null);
        }
        return null;
    }

    public static boolean hasTag(PsiDocComment docComment, String tagName) {
        if (docComment == null) {
            return false;
        }
        PsiDocTag[] tags = docComment.getTags();
        return ArrayUtil.firstMatch(i -> StrUtil.equals(i.getName(), tagName), tags) != null;
    }

    /**
     * 注释中获取详情
     *
     * @param docComment
     * @return
     */
    public static String parseDesc(PsiDocComment docComment) {

        if (docComment != null) {
            PsiElement[] descriptionElements = docComment.getDescriptionElements();
            StringBuilder builder = new StringBuilder();
            for (PsiElement descriptionElement : descriptionElements) {
                String text = descriptionElement.getText();
                if (StrUtil.isNotBlank(text)) {
                    builder.append(text);
                }
            }
            return builder.toString();
        }
        return null;
    }
}
