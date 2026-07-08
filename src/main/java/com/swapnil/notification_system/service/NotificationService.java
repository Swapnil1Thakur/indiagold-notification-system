package com.swapnil.notification_system.service;

import com.swapnil.notification_system.dto.NotificationRequest;
import com.swapnil.notification_system.dto.NotificationResponse;
import com.swapnil.notification_system.repository.NotificationHistoryRepository;
import com.swapnil.notification_system.repository.UserPreferenceRepository;
import com.swapnil.notification_system.repository.UserRepository;
import com.swapnil.notification_system.sender.EmailNotificationSender;
import com.swapnil.notification_system.sender.InAppNotificationSender;
import com.swapnil.notification_system.sender.PushNotificationSender;
import com.swapnil.notification_system.sender.SMSNotificationSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final EmailNotificationSender emailNotificationSender;

    private final SMSNotificationSender smsNotificationSender;
    private final PushNotificationSender pushNotificationSender;
    private final InAppNotificationSender inAppNotificationSender;


    public NotificationService(UserRepository userRepository,
                               UserPreferenceRepository userPreferenceRepository,
                               NotificationHistoryRepository notificationHistoryRepository,
                               EmailNotificationSender emailNotificationSender,
                               SMSNotificationSender smsNotificationSender,
                               PushNotificationSender pushNotificationSender,
                               InAppNotificationSender inAppNotificationSender) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.notificationHistoryRepository = notificationHistoryRepository;
        this.emailNotificationSender = emailNotificationSender;
        this.smsNotificationSender = smsNotificationSender;
        this.pushNotificationSender = pushNotificationSender;
        this.inAppNotificationSender = inAppNotificationSender;
    }

    public NotificationResponse sendNotification(NotificationRequest request){
        throw new UnsupportedOperationException("Method implementation is pending");
    }
}
