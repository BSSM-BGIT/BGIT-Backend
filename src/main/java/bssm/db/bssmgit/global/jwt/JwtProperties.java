package bssm.db.bssmgit.global.jwt;

public class JwtProperties {
    public static final long ACCESS_TOKEN_VALID_TIME = 60 * 30; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 14; // 2주
    public static final String JWT_ACCESS = "ACCESS-TOKEN";
}
