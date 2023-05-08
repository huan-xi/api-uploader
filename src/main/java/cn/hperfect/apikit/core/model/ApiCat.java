package cn.hperfect.apikit.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * api分类
 *
 * @author huanxi
 * @version 1.0
 * @date 2021/2/4 10:58 上午
 */
@Data
public class ApiCat implements Serializable {

    private String catName;

    private List<String> path;
    /**
     * 分类详情描述
     */
    private String catDesc;
    /**
     * api
     */
    private List<ApiModel> apiModels;

    /**
     * 子类
     */
    private List<ApiCat> children;
}
