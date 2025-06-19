package com.api.meetudy.study.group.dto;

import com.api.meetudy.study.group.enums.Location;
import com.api.meetudy.study.group.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class StudyGroupUpdateDto {

    @Schema(description = "The name of the study group.")
    private String name;

    @Schema(description = "Description of the study group.")
    private String description;

    @Schema(description = "Maximum number of participants.")
    private Integer maxParticipants;

    @Schema(description = "Indicates whether the study is conducted online or offline.")
    private Boolean isOnline;

    @Schema(description = "Location of the study.")
    private Location location;

    @Schema(description = "The category or type of the study group.",
            example = "LANGUAGE, CERTIFICATION")
    private StudyCategory category;

}