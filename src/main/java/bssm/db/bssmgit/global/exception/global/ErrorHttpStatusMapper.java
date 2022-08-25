package bssm.db.bssmgit.global.exception.global;

import bssm.db.bssmgit.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ErrorHttpStatusMapper {

    public static HttpStatus mapToStatus(ErrorCode errorCode) {
        switch (errorCode) {

            case NOT_MATCH_PASSWORD:
            case ALREADY_EXISTS_USER:
            case NOT_MATCH_CODE:
            case NOT_MATCH_ACCOUNT:
            case USER_NOT_FOUND:
            case POSTS_NOT_FOUND:
            case COMMENTS_NOT_FOUND:
                return HttpStatus.BAD_REQUEST;

            case USER_NOT_LOGIN:
                return HttpStatus.UNAUTHORIZED;

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}