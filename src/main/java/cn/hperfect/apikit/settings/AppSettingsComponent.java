// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package cn.hperfect.apikit.settings;

import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.Function;
import com.intellij.util.ui.FormBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置面板
 *
 * @author huanxi
 */
@Getter
@Setter
public class AppSettingsComponent {

    private final JPanel mainPanel;
    @Getter
    private Map<EditorTextField, GetterSetter> editorSettingMap = new HashMap<>();


    public AppSettingsComponent(AppSettingsState settings) {

        editorSettingMap.put(getEditorTextField("https://doc.domain.com"), new GetterSetter("yapi地址(不要加/结尾):",settings::getYapiHost, settings::setYapiHost));
        editorSettingMap.put(new EditorTextField(), new GetterSetter("yapi token:",settings::getYapiToken, settings::setYapiToken));
        editorSettingMap.put(new EditorTextField(), new GetterSetter("yapi项目id:",settings::getYapiProjectId, settings::setYapiProjectId));
        editorSettingMap.put(new EditorTextField("以逗号连接"), new GetterSetter("全局排除字段(逗号连接):",settings::getExcludeFields, settings::setExcludeFields));

        FormBuilder formBuilder = FormBuilder.createFormBuilder();

        editorSettingMap.forEach((k, v) -> {
            formBuilder.addLabeledComponent(new JBLabel(v.getLabel()), k, 1, false);
        });

        mainPanel = formBuilder
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @NotNull
    private static EditorTextField getEditorTextField(String placeholder) {
        EditorTextField textField = new EditorTextField();
        textField.setPlaceholder(placeholder);
        return textField;
    }

}
