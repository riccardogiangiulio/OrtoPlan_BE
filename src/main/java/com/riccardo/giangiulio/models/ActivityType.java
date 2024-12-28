package com.riccardo.giangiulio.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityType {

    @NonNull
    private long activityTypeId;

    @NonNull
    private String name;

    public ActivityType(@NonNull String name){
        this.name = name;
    }
}
