package cn.hperfect.apikit.core.model;

import cn.hperfect.apikit.enums.ApiParamType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * api参数节点(递归)
 * 基础类型
 * 对象类型
 * 数组类型
 * map类型
 *
 * @author huanxi
 * @version 1.0
 * @date 2021/2/2 4:26 下午
 */
@Data
public abstract class BaseApiPropNode implements Serializable {

    /**
     * 参数字段名(默认为参数名),只有基本数据类型才需要
     */
    private String name;

    /**
     * Object
     * 参数类型(最终为string)
     */
    private ApiParamType type;
    /**
     * 如果type==array 的子元素的类型,第0个
     * type 非数组,则属性
     */
    private List<BaseApiPropNode> paramModelList = new ArrayList<>();
    /**
     * 参数描述
     */
    private String desc;
    /**
     * 是否位必须值
     */
    private boolean required;
    /**
     * 默认值
     */

    private Object defaultValue;
    /**
     * 是否过期
     */
    private boolean deprecated;
    /**
     * mock 表达式
     */
    private String mockExpr;

    /**
     * 示例
     */
    private String example;


}
