package com.swapnil.notification_system.service;

import com.swapnil.notification_system.dto.NotificationRequest;
import com.swapnil.notification_system.dto.NotificationResponse;
import com.swapnil.notification_system.entity.NotificationHistory;
import com.swapnil.notification_system.entity.User;
import com.swapnil.notification_system.entity.UserPreference;
import com.swapnil.notification_system.enums.NotificationChannel;
import com.swapnil.notification_system.repository.NotificationHistoryRepository;
import com.swapnil.notification_system.repository.UserPreferenceRepository;
import com.swapnil.notification_system.repository.UserRepository;
import com.swapnil.notification_system.sender.EmailNotificationSender;
import com.swapnil.notification_system.sender.InAppNotificationSender;
import com.swapnil.notification_system.sender.PushNotificationSender;
import com.swapnil.notification_system.sender.SMSNotificationSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)               //this will enable Mockito support in JUnit 5
public class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;  //fake repository

    @Mock
    private UserPreferenceRepository userPreferenceRepository;  //fake UserPreferenceRepository

    @Mock
    private NotificationHistoryRepository notificationHistoryRepository;   // fake NotificationHistoryRepository

    @Mock
    private EmailNotificationSender emailNotificationSender;     // fake Email Sender

    @Mock
    private SMSNotificationSender smsNotificationSender;         // fake SMS Sender

    @Mock
    private PushNotificationSender pushNotificationSender;       // fake Push Sender

    @Mock
    private InAppNotificationSender inAppNotificationSender;     // fake In-App Sender

    @InjectMocks
    private NotificationService notificationService;             // creates NotificationService and injects all mocks

    @Test
    void shouldSendNotificationsBasedOnUserPreferences() {

        // ---------- Arrange ----------

        User user = new User();
        user.setId(1L);
        user.setName("Swapnil");
        user.setEmail("swapnil@example.com");
        user.setPhoneNumber("9876543210");

        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setEmailEnabled(true);
        preference.setSmsEnabled(false);
        preference.setPushEnabled(true);
        preference.setInAppEnabled(true);

        NotificationRequest request = new NotificationRequest(
                1L,
                "Gold Price Alert",
                "Gold price increased",
                List.of(
                        NotificationChannel.EMAIL,
                        NotificationChannel.SMS,
                        NotificationChannel.PUSH,
                        NotificationChannel.IN_APP
                )
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.of(preference));

        // ---------- Act ----------

        NotificationResponse response =
                notificationService.sendNotification(request);

        // ---------- Assert ----------

        assertEquals(3, response.getSuccessful());
        assertEquals(0, response.getFailed());
        assertEquals(1, response.getSkipped());

        verify(emailNotificationSender, times(1))
                .send(user, request.getTitle(), request.getBody());

        verify(smsNotificationSender, never())
                .send(any(), any(), any());

        verify(pushNotificationSender, times(1))
                .send(user, request.getTitle(), request.getBody());

        verify(inAppNotificationSender, times(1))
                .send(user, request.getTitle(), request.getBody());

        verify(notificationHistoryRepository, times(4))
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // ---------- Arrange ----------

        NotificationRequest request = new NotificationRequest(
                1L,
                "Gold Price Alert",
                "Gold price increased",
                List.of(NotificationChannel.EMAIL)
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ---------- Act & Assert ----------

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.sendNotification(request)
        );

        assertEquals("User not found", exception.getMessage());

        // ---------- Verify ----------

        verify(userPreferenceRepository, never()).findByUserId(anyLong());

        verify(notificationHistoryRepository, never()).save(any());

        verifyNoInteractions(
                emailNotificationSender,
                smsNotificationSender,
                pushNotificationSender,
                inAppNotificationSender
        );
    }

    @Test
    void shouldThrowExceptionWhenUserPreferenceDoesNotExist() {

        // ---------- Arrange ----------

        User user = new User();
        user.setId(1L);
        user.setName("Swapnil");
        user.setEmail("swapnil@example.com");
        user.setPhoneNumber("9876543210");

        NotificationRequest request = new NotificationRequest(
                1L,
                "Gold Price Alert",
                "Gold price increased",
                List.of(NotificationChannel.EMAIL)
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        // ---------- Act & Assert ----------

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.sendNotification(request)
        );

        assertEquals("User preferences not found", exception.getMessage());

        // ---------- Verify ----------

        verify(notificationHistoryRepository, never()).save(any());

        verifyNoInteractions(
                emailNotificationSender,
                smsNotificationSender,
                pushNotificationSender,
                inAppNotificationSender
        );
    }

    @Test
    void shouldSkipDisabledNotificationChannels() {

        // ---------- Arrange ----------

        User user = new User();
        user.setId(1L);
        user.setName("Swapnil");
        user.setEmail("swapnil@example.com");
        user.setPhoneNumber("9876543210");

        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setEmailEnabled(false);
        preference.setSmsEnabled(false);
        preference.setPushEnabled(false);
        preference.setInAppEnabled(true);

        NotificationRequest request = new NotificationRequest(
                1L,
                "Gold Price Alert",
                "Gold price increased",
                List.of(
                        NotificationChannel.EMAIL,
                        NotificationChannel.SMS,
                        NotificationChannel.PUSH,
                        NotificationChannel.IN_APP
                )
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.of(preference));

        // ---------- Act ----------

        NotificationResponse response =
                notificationService.sendNotification(request);

        // ---------- Assert ----------

        assertEquals(1, response.getSuccessful());
        assertEquals(0, response.getFailed());
        assertEquals(3, response.getSkipped());

        verify(emailNotificationSender, never())
                .send(any(), any(), any());

        verify(smsNotificationSender, never())
                .send(any(), any(), any());

        verify(pushNotificationSender, never())
                .send(any(), any(), any());

        verify(inAppNotificationSender, times(1))
                .send(user, request.getTitle(), request.getBody());

        verify(notificationHistoryRepository, times(4))
                .save(any());
    }

    @Test
    void shouldSaveNotificationHistoryForEveryRequestedChannel() {

        // ---------- Arrange ----------

        User user = new User();
        user.setId(1L);
        user.setName("Swapnil");
        user.setEmail("swapnil@example.com");
        user.setPhoneNumber("9876543210");

        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setEmailEnabled(true);
        preference.setSmsEnabled(true);
        preference.setPushEnabled(true);
        preference.setInAppEnabled(true);

        NotificationRequest request = new NotificationRequest(
                1L,
                "Gold Price Alert",
                "Gold price increased",
                List.of(
                        NotificationChannel.EMAIL,
                        NotificationChannel.SMS,
                        NotificationChannel.PUSH,
                        NotificationChannel.IN_APP
                )
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.of(preference));

        // ---------- Act ----------

        notificationService.sendNotification(request);

        // ---------- Assert ----------

        verify(notificationHistoryRepository, times(4))
                .save(any(NotificationHistory.class));
    }

}
