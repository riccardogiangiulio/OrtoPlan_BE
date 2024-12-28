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
public class Activity {

    @NonNull
    private long activityId;

    @NonNull
    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduled_dt;

    @Builder.Default
    private boolean completed = false;

    @NonNull
    private ActivityType activityType;

    @NonNull
    private Plantation plantation;

    public Activity(@NonNull String description, @NonNull LocalDateTime scheduled_dt, @NonNull ActivityType activityType, @NonNull Plantation plantation){
        this.description = description;
        this.scheduled_dt = scheduled_dt;
        this.activityType = activityType;
        this.plantation = plantation;
    }
}
