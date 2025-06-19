package com.api.meetudy.member.dto;

import com.api.meetudy.global.utils.ValidEnum;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.group.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class SignUpDto {

    @NotBlank
    @Email(message = "Invalid email format.")
    @Schema(description = "The email address of the user.",
            example = "user@example.com")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z0-9]{5,15}$",
            message = "Username must be 5 to 15 characters long and consist of only letters and numbers."
    )
    @Schema(description = "The unique username of the user. It should be between 5 to 15 characters and consist only of letters and numbers.",
            example = "user123")
    private String username;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,17}$",
            message = "Password must be 8 to 17 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    @Schema(description = "The password for the user account. It must include at least one uppercase letter, one lowercase letter, one number, and one special character. The length should be between 8 and 17 characters.",
            example = "Password123!")
    private String password;

    @NotBlank
    @Schema(description = "The nickname of the user.",
            example = "user")
    private String nickname;

    @NotBlank
    @Schema(description = "The user's major or field of study.",
            example = "Computer Science")
    private String major;

    @Schema(description = "A brief introduction of the user. This field is optional and can be left empty.",
            example = "A passionate software developer.")
    private String introduction;

    @NotNull
    @Schema(description = "Indicates whether the user is online or offline. The value should be 'true' for online and 'false' for offline.",
            example = "true")
    private Boolean isOnline;

    @NotNull
    @Schema(description = "Indicates whether the user wants to enable notifications. The value should be 'true' to enable and 'false' to disable.",
            example = "true")
    private Boolean notificationEnabled;

    @ValidEnum(target = Location.class)
    @Schema(description = "The geographical location of the user.",
            example = "SEOUL")
    private Location location;

    @Size(max = 5)
    @NotEmpty(message = "Study categories cannot be empty.")
    @Schema(description = "A list of study categories that the user is interested in. The user can select up to 5 categories.",
            example = "[\"LANGUAGE\", \"CERTIFICATION\"]")
    private List<StudyCategory> studyCategories;

}