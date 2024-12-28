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
public class Plantation {

    @NonNull
    private long plantationId;

    @NonNull
    private String name;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NonNull
    private String city;

    @NonNull
    private User user;

    @NonNull
    private Plant plant;

    public Plantation(@NonNull String name, @NonNull LocalDate startDate, @NonNull LocalDate endDate, @NonNull String city, @NonNull User user, @NonNull Plant plant){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.user = user;
        this.plant = plant;
    }


}
