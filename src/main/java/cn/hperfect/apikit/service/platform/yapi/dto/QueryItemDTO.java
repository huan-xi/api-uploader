package cn.hperfect.apikit.service.platform.yapi.dto;

import lombok.Data;

/**
 * @author huanxi
 * @version 1.0
 * @date 2021/2/3 3:32 下午
 */
@Data
public class QueryItemDTO {
    protected String desc;
    protected String example;
    protected String name;
    protected String required = "0";
}
