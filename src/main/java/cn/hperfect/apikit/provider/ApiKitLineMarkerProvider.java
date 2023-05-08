package cn.hperfect.apikit.provider;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.content.Content;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

/**
 * 接口前面配置解析按钮
 */
public class ApiKitLineMarkerProvider implements LineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement psiElement) {
        LineMarkerInfo<PsiElement> lineMarkerInfo = null;

        if (psiElement instanceof PsiIdentifier && psiElement.getParent() instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement.getParent();
            if (!isApiMethod(psiMethod)) {
                return null;
            }
      /*      lineMarkerInfo = new LineMarkerInfo<>(psiElement, psiElement.getTextRange(), PluginIcons.fastRequest_editor,
                    new FunctionTooltip(methodElement),
                    (e, elt) -> {
                        Project project = elt.getProject();
                        GeneratorUrlService generatorUrlService = ApplicationManager.getApplication().getService(GeneratorUrlService.class);
                        generatorUrlService.generate(methodElement);

                        //打开工具窗口
                        ToolWindow fastRequestToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Fast Request");
                        if (fastRequestToolWindow != null && !fastRequestToolWindow.isActive()) {
                            fastRequestToolWindow.activate(null);
                            Content content = fastRequestToolWindow.getContentManager().getContent(0);
                            assert content != null;
                            fastRequestToolWindow.getContentManager().setSelectedContent(content);
                        }
                        //send message to change param
                        MessageBus messageBus = project.getMessageBus();
                        messageBus.connect();
                        ConfigChangeNotifier configChangeNotifier = messageBus.syncPublisher(ConfigChangeNotifier.PARAM_CHANGE_TOPIC);
                        configChangeNotifier.configChanged(true, project.getName());
                    },
                    GutterIconRenderer.Alignment.LEFT, () -> "fastRequest");
            lineMarkerInfo.createGutterRenderer();*/
            return lineMarkerInfo;
        }
        return null;
    }

    /**
     * 判断是否是api 方法
     *
     * @param psiMethod
     * @return
     */
    private boolean isApiMethod(PsiMethod psiMethod) {
        return false;
    }


}
