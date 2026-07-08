package com.swapnil.notification_system.sender;

import com.swapnil.notification_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SMSNotificationSender implements NotificationSender{

    @Override
    public void send(User user, String title, String body) {

        System.out.println(" SMS NOTIFICATION ");

        System.out.println("Phone Number: " + user.getPhoneNumber());   //recipient phone number

        System.out.println("Title: " + title);

        System.out.println("Message: " + body);

        System.out.println("--------");

    }
}
