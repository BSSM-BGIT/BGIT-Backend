package bssm.db.bssmgit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생했습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),

    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),

    ALREADY_EXISTS_USER(422, "이미 존재하는 회원입니다."),
    NOT_MATCH_ACCOUNT(401, "이메일 또는 비밀번호가 일치하지 않습니다."),


    NOT_MATCH_PASSWORD(401, "비밀번호가 일치하지 않습니다."),
    NOT_MATCH_CODE(401, "이메일 인증 코드가 일치하지 않습니다."),

    USER_NOT_LOGIN(403, "로그인 후 이용해주세요"),

    DONT_ACCESS_OTHER(400, "자신의 게시글만 삭제할 수 있습니다."),
    POSTS_NOT_FOUND(404, "존재하지 않는 게시글입니다."),
    COMMENTS_NOT_FOUND(404, "해당 댓글이 존재하지 않습니다."),

    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_TOKEN(401, "무효한 토큰입니다."),
    NOT_MATCH_TOKEN(402, "토큰이 일치하지 않습니다."),
    RETRY_LOGIN(402, "로그인을 다시 해주세요.");

    private final int code;
    private final String message;

}
