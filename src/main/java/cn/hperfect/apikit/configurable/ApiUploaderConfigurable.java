package cn.hperfect.apikit.configurable;

import cn.hperfect.apikit.settings.AppSettingsComponent;
import cn.hperfect.apikit.settings.AppSettingsState;
import cn.hperfect.apikit.settings.GetterSetter;
import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ApiUploaderConfigurable implements Configurable {
    private AppSettingsComponent settingsComponent;
    private final AppSettingsState settings;

    public ApiUploaderConfigurable(Project project) {
        settings = project.getService(AppSettingsState.class);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Api-uploader Configuration";
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new AppSettingsComponent(settings);
        return settingsComponent.getMainPanel();
    }

    @Override
    public boolean isModified() {
        if (settingsComponent == null) {
            return false;
        }
        Map<EditorTextField, GetterSetter> editorSettingMap = settingsComponent.getEditorSettingMap();
        for (Map.Entry<EditorTextField, GetterSetter> entry : editorSettingMap.entrySet()) {
            GetterSetter value = entry.getValue();
            EditorTextField key = entry.getKey();
            if (!StrUtil.equals(value.getGetter().get(), key.getText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        Map<EditorTextField, GetterSetter> editorSettingMap = settingsComponent.getEditorSettingMap();
        for (Map.Entry<EditorTextField, GetterSetter> entry : editorSettingMap.entrySet()) {
            GetterSetter value = entry.getValue();
            EditorTextField key = entry.getKey();
            value.getSetter().accept(key.getText());
        }
    }

    @Override
    public void reset() {
        Map<EditorTextField, GetterSetter> editorSettingMap = settingsComponent.getEditorSettingMap();
        for (Map.Entry<EditorTextField, GetterSetter> entry : editorSettingMap.entrySet()) {
            GetterSetter value = entry.getValue();
            EditorTextField key = entry.getKey();
            key.setText(value.getGetter().get());
        }
    }


    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }


}
