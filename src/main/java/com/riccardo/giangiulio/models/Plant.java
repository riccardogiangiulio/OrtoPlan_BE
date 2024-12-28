package com.riccardo.giangiulio.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plant {

    @NonNull
    private long plantId;

    @NonNull
    private String name;

    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate cultivationStart;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate cultivationEnd;

    @NonNull
    private int harvestTime;

    public Plant(@NonNull String name, String description, @NonNull LocalDate cultivationStart, @NonNull LocalDate cultivationEnd, @NonNull int harvestTime){
        this.name = name;
        this.description = description;
        this.cultivationStart = cultivationStart;
        this.cultivationEnd = cultivationEnd;
        this.harvestTime = harvestTime;
    }

}
