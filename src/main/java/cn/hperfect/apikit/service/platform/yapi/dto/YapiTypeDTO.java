package cn.hperfect.apikit.service.platform.yapi.dto;

import com.google.gson.annotations.SerializedName;
import com.intellij.mock.Mock;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author huanxi
 * @version 1.0
 * @date 2021/2/3 4:08 下午
 */
@Data
public class YapiTypeDTO {
    private String type;
    private Map<String, YapiTypeDTO> properties;
    private String title;
    private String description;
    private YapiTypeDTO items;
//    private String required = "0";
    @SerializedName("enum")
    private List<String> enumList;
    private String enumDesc;
    private Mock mock;

    @Data
    public static class Mock {
        private final String mock;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }
}
