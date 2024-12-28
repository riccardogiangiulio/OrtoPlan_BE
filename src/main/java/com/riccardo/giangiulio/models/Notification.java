package com.riccardo.giangiulio.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @NonNull
    private long notificationId;

    @NonNull
    private String message;

    @Builder.Default
    private boolean opened = false;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sent_dt;

    @NonNull
    private Activity activity;
    
    @NonNull
    private User user;

    public Notification(@NonNull String message, @NonNull LocalDateTime sent_dt, @NonNull Activity activity, @NonNull User user){
        this.message = message;
        this.sent_dt = sent_dt;
        this.activity = activity;
        this.user = user;
    }
}
