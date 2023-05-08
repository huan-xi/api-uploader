package cn.hperfect.apikit.actions;

import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.core.parser.ApiKitParserFactory;
import cn.hperfect.apikit.core.parser.IControllerParser;
import cn.hperfect.apikit.service.BaseApiUploadService;
import cn.hperfect.apikit.service.platform.yapi.YapiUploadService;
import cn.hperfect.apikit.utils.NotificationUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class YApiUploadAction extends BaseApiUploadService {

    final IControllerParser controllerParser = ApiKitParserFactory.getControllerParser();


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ApiKitParserFactory.setProject(e.getProject());
        Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        PsiFile psiFile = e.getDataContext().getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass selectedClass = PsiTreeUtil.getContextOfType(referenceAt, PsiClass.class);
        PsiMethod selectedMethod = PsiTreeUtil.getContextOfType(referenceAt, PsiMethod.class);
        if (selectedClass == null) {
            NotificationUtils.notifyError(null, "请选择正确的控制器class");
            return;
        }

        YapiUploadService yapiUploadService = new YapiUploadService(e.getProject());
        ApiCat apiCat = controllerParser.parse(selectedClass, selectedMethod);
        //上传到yapi
        yapiUploadService.uploadAsync(e.getProject(), apiCat);
    }


}
