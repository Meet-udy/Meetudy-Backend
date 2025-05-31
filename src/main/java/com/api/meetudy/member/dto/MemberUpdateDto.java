package com.api.meetudy.member.dto;

import com.api.meetudy.study.group.enums.Location;
import com.api.meetudy.study.group.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberUpdateDto {

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,17}$",
            message = "Password must be 8 to 17 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    @Schema(description = "The password for the user account. It must include at least one uppercase letter, one lowercase letter, one number, and one special character. The length should be between 8 and 17 characters.",
            example = "Password123!")
    private String password;

    @Schema(description = "The nickname of the user.",
            example = "user")
    private String nickname;

    @Schema(description = "The user's major or field of study.",
            example = "Computer Science")
    private String major;

    @Schema(description = "A brief introduction of the user. This field is optional and can be left empty.",
            example = "A passionate software developer.")
    private String introduction;

    /*@Schema(description = "The URL or path to the user's profile image.",
            example = "https://example.com/profile-images/user123.jpg")
    private String profileImage;*/

    @Schema(description = "Indicates whether the user is online or offline. The value should be 'true' for online and 'false' for offline.",
            example = "true")
    private Boolean isOnline;

    @Schema(description = "Indicates whether the user wants to enable notifications. The value should be 'true' to enable and 'false' to disable.",
            example = "true")
    private Boolean notificationEnabled;

    @Schema(description = "The geographical location of the user.",
            example = "SEOUL")
    private Location location;

    @Size(max = 5)
    @Schema(description = "A list of study categories that the user is interested in. The user can select up to 5 categories.",
            example = "[\"LANGUAGE\", \"CERTIFICATION\"]")
    private List<StudyCategory> interests;

}
