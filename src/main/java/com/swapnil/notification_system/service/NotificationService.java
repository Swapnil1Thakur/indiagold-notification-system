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
import com.swapnil.notification_system.entity.User;
import com.swapnil.notification_system.entity.UserPreference;

import java.util.Optional;

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

        // find user using the user ID from the request
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not  found"));

        // find notification preferences for the user
        UserPreference userPreference = userPreferenceRepository.findByUserId(user.getId())
                .orElseThrow(()-> new RuntimeException("User preferences not found"));

        //business logic in next step
        //iterating through all reqeusted notification channels

        for(var channel : request.getChannels()){
            switch (channel){
                case EMAIL :
                    if(userPreference.isEmailEnabled()){
                        emailNotificationSender.send(user,request.getTitle(), request.getBody());
                    }

                    break;
                case SMS:
                    if(userPreference.isSmsEnabled()){
                        smsNotificationSender.send(user, request.getTitle(),  request.getBody());



                    }
                    break;

                    case IN_APP:
                    if(userPreference.isInAppEnabled()){
                        inAppNotificationSender.send(user, request.getTitle(), request.getBody());
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported notification channel.");

            }
        }

        throw new UnsupportedOperationException("Method implementation is pending");

    }
}
