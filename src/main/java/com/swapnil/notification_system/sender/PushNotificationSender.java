package com.swapnil.notification_system.sender;

import com.swapnil.notification_system.entity.User;

public class PushNotificationSender implements NotificationSender{
    @Override
    public void send(User user, String title, String body) {
        System.out.println("PUSH NOTIFICATION");

        System.out.println("User ID: " + user.getId());

        System.out.println("Title: " + title);

        System.out.println("Message: " + body);


    }
}
