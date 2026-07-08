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
import com.swapnil.notification_system.entity.NotificationHistory;
import com.swapnil.notification_system.enums.DeliveryStatus;
import com.swapnil.notification_system.enums.NotificationChannel;

import java.time.LocalDateTime;


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
                .orElseThrow(() -> new RuntimeException("User not found"));

        // find notification preferences for the user
        UserPreference userPreference = userPreferenceRepository.findByUserId(user.getId())
                .orElseThrow(()-> new RuntimeException("User preferences not found"));

        //counter to track notification processing the results
        int successful = 0;
        int failed = 0;
        int skipped = 0;


        //business logic in next step
        //iterating through all reqeusted notification channels

        for(NotificationChannel channel : request.getChannels()){
            switch (channel) {

                case EMAIL:

                    if (userPreference.isEmailEnabled()) {

                        emailNotificationSender.send(user, request.getTitle(), request.getBody());

                        saveNotificationHistory(
                                user,
                                NotificationChannel.EMAIL,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SUCCESS
                        );

                        successful++;

                    } else {

                        saveNotificationHistory(
                                user,
                                NotificationChannel.EMAIL,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SKIPPED
                        );
                        skipped++;
                    }

                    break;

                case SMS:

                    if (userPreference.isSmsEnabled()) {

                        smsNotificationSender.send(user, request.getTitle(), request.getBody());

                        saveNotificationHistory(
                                user,
                                NotificationChannel.SMS,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SUCCESS
                        );
                        successful++;

                    } else {

                        saveNotificationHistory(
                                user,
                                NotificationChannel.SMS,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SKIPPED
                        );
                        skipped++;
                    }

                    break;

                case PUSH:

                    if (userPreference.isPushEnabled()) {

                        pushNotificationSender.send(user, request.getTitle(), request.getBody());

                        saveNotificationHistory(
                                user,
                                NotificationChannel.PUSH,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SUCCESS
                        );
                        successful++;

                    } else {

                        saveNotificationHistory(
                                user,
                                NotificationChannel.PUSH,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SKIPPED
                        );
                        skipped++;
                    }

                    break;

                case IN_APP:

                    if (userPreference.isInAppEnabled()) {

                        inAppNotificationSender.send(user, request.getTitle(), request.getBody());

                        saveNotificationHistory(
                                user,
                                NotificationChannel.IN_APP,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SUCCESS
                        );
                        successful++;

                    } else {

                        saveNotificationHistory(
                                user,
                                NotificationChannel.IN_APP,
                                request.getTitle(),
                                request.getBody(),
                                DeliveryStatus.SKIPPED
                        );
                        skipped++;
                    }

                    break;

                default:
                    throw new IllegalArgumentException("Unsupported notification channel.");
            }
        }

        //throw new UnsupportedOperationException("Method implementation is pending");
        return new NotificationResponse(
                "Notification processing completed",
                successful,
                failed,
                skipped
        );

    }

    //helper method to save notification history
    private void saveNotificationHistory(User user,
                                         NotificationChannel channel,
                                         String title,
                                         String body,
                                         DeliveryStatus status) {

        NotificationHistory history = new NotificationHistory();

        history.setUser(user);

        history.setChannel(channel);

        history.setTitle(title);

        history.setBody(body);

        history.setStatus(status);

        history.setSentAt(LocalDateTime.now());

        notificationHistoryRepository.save(history);
    }
}
