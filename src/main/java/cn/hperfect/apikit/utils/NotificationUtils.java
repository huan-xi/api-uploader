package cn.hperfect.apikit.utils;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;


public class NotificationUtils {

    public static void notifyError(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("API KIT Notification Group")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    public static void info(Project project, String content) {
        if (project == null) {
            return;
        }
        NotificationGroupManager.getInstance()
                .getNotificationGroup("API KIT Notification Group")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }
}
