package cn.hperfect.apikit.core.parser;

import cn.hperfect.apikit.core.model.type.ApiObjectPropNode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hperfect
 * @date 2023/5/7 17:19
 */
@Data
public class ParseTypeContext {

    Map<String, ApiObjectPropNode> nodeMap = new HashMap<>();

    public ApiObjectPropNode getClassNode(String qualifiedName) {
        return nodeMap.get(qualifiedName);
    }

    public void putClassNode(String key, ApiObjectPropNode node) {
        nodeMap.put(key, node);
    }
}
