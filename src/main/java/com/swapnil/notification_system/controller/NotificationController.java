package com.swapnil.notification_system.controller;

import com.swapnil.notification_system.dto.NotificationRequest;
import com.swapnil.notification_system.dto.NotificationResponse;
import com.swapnil.notification_system.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @Valid @RequestBody NotificationRequest request){    //this validates and maps incoming JSON request

        NotificationResponse response = notificationService.sendNotification(request);    //sends the request to the service layer

        return ResponseEntity.ok(response);    //this returns HTTP 200 with the response body

    }

}
