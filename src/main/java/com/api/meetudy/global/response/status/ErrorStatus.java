package com.api.meetudy.global.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    // Authorization
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH400", "The token is invalid."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "AUTH401", "Invalid JWT signature."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH402", "The token has expired."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "AUTH403", "Unsupported JWT."),
    JWT_CLAIMS_EMPTY(HttpStatus.BAD_REQUEST, "AUTH404", "JWT claims is empty."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "AUTH405",  "The user is not authenticated."),
    REFRESH_TOKEN_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "AUTH406", "The refresh token has already been logged out."),
    GROUP_LEADER_ACCESS_ONLY(HttpStatus.UNAUTHORIZED, "AUTH407", "The functionality is accessible only to group leaders."),
    VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH408", "The verification failed."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER400", "Member not found."),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER401", "The username already exists."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "MEMBER402", "The password does not match."),
    SEND_EMAIL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER403", "Failed to send email."),

    // Study Group
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP400", "Group not found."),
    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP401", "Group member not found."),
    MAX_PARTICIPANTS_EXCEEDED(HttpStatus.BAD_REQUEST, "GROUP402", "Study group has already reached the maximum number of participants."),
    REQUEST_ALREADY_SENT(HttpStatus.BAD_REQUEST, "GROUP403", "Your participation request for this study group has already been sent."),
    NO_SUCH_MEMBER_IN_STUDY_GROUP(HttpStatus.NOT_FOUND, "GROUP404", "The member cannot be found in the study group."),
    CANNOT_LEAVE_THE_GROUP(HttpStatus.BAD_REQUEST, "GROUP405", "Group leader cannot leave the group. Please delegate leadership first."),

    // Chat
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT500", "The chat room not found."),
    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT501", "The chat room member not found."),
    INSUFFICIENT_CHAT_MEMBERS(HttpStatus.BAD_REQUEST, "CHAT502", "At least two members are required to create a chat room."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}