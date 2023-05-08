package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;

/**
 * @author hperfect
 * @date 2023/5/7 14:55
 */
public class ApiFilePropNode extends BaseApiPropNode {
    public ApiFilePropNode() {
        setType(ApiParamType.FILE);
    }
}
