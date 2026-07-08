package com.swapnil.notification_system.sender;

import com.swapnil.notification_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class InAppNotificationSender implements NotificationSender{

    @Override
    public void send(User user, String title, String body) {

        System.out.println("IN APP NOTIFICATION");

        System.out.println("User ID: " + user.getId());

        System.out.println("Title: " + title);

        System.out.println("Message: " + body);
    }
}
