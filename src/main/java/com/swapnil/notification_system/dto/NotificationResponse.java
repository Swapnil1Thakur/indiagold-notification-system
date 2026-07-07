package com.swapnil.notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    //overall response message
    private String message;
    private int successful;
    private int failed;

    //skipped because of user preferences
    private int skipped;
}
