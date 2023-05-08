package cn.hperfect.apikit.core.model;

import cn.hperfect.apikit.enums.ApiType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.http.client.utils.CloneUtils;
import org.codehaus.groovy.ast.tools.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * api 模型
 *
 * @author huanxi
 * @version 1.0
 * @date 2021/2/2 4:23 下午
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ApiModel implements Serializable, Cloneable {
    static final long serialVersionUID = -1L;

    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * query,参数
     */
    private List<BaseApiPropNode> query;

    /**
     * body参数
     */
    private BaseApiPropNode body;
    /**
     * get,post,del,put
     */
    private ApiType apiType;
    /**
     * 描述
     */
    private String desc;

    /**
     * 返回值
     */
    private BaseApiPropNode resultType;

    /**
     * 是否标记过期
     */
    private Boolean deprecated;

    public Boolean getDeprecated() {
        if (deprecated == null) {
            return false;
        }
        return deprecated;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
