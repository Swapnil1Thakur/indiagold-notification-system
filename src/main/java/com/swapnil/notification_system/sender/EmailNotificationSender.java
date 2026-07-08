package com.swapnil.notification_system.sender;

import com.swapnil.notification_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender{


    @Override
    public void send(User user, String title, String body) {

        System.out.println(" EMAIL NOTIFICATION ");

        System.out.println("To: " + user.getEmail());    //recipient email

        System.out.println("Title: " + title);                //notification title

        System.out.println("Message: " + body);            //notification body

        System.out.println("-----\n");
    }
}
