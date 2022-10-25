package bssm.db.bssmgit.global.jwt;

public class JwtProperties {
    public static final long ACCESS_TOKEN_VALID_TIME = 60 * 30 * 1000; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 1209600 * 1000; // 2주
    public static final String JWT_ACCESS = "ACCESS-TOKEN";
}
