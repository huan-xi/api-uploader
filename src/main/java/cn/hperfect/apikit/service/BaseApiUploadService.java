package cn.hperfect.apikit.service;

import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.pojo.result.ApiUploadResult;
import cn.hperfect.apikit.core.model.ApiModel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * @author hperfect
 * @date 2023/5/7 14:18
 */
public abstract class BaseApiUploadService extends AnAction {
    /**
     * 异步上传模板方法
     *
     * @param project       项目
     * @param apiCat        待处理接口分类
     * @param apiHandle     单个接口数据消费者
     * @param afterCallback 所有接口列表处理完毕后的回调执行，用于关闭资源
     */
    protected void handleUploadAsync(Project project, ApiCat apiCat,
                                     BiFunction<ApiCat, ProgressIndicator, ApiUploadResult> apiHandle,
                                     Supplier<?> afterCallback) {
        // 异步处理
        ProgressManager.getInstance().run(new Task.Backgroundable(project, " DefaultConstants.NAME") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(false);
            }
        });
    }
}
