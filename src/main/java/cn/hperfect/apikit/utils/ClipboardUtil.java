package cn.hperfect.apikit.utils;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.util.ui.TextTransferable;

/**
 * @author hperfect
 * @date 2023/5/7 10:52
 */
public class ClipboardUtil {

    public static void setStr(String text) {
        CopyPasteManager.getInstance().setContents(new TextTransferable(text));
    }
}
