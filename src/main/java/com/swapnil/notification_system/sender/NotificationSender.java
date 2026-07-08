package com.swapnil.notification_system.sender;

import com.swapnil.notification_system.entity.User;

public interface NotificationSender {
    void send(User user, String title, String body);   //this will be common for all the notification channels
}
