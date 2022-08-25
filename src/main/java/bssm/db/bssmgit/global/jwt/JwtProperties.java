package bssm.db.bssmgit.global.jwt;

public class JwtProperties {
    public static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    public static final long REFRESH_TOKEN_VALID_TIME = 30 * 24 * 60 * 60 * 1000L;
    public static final String JWT_ACCESS = "ACCESS-TOKEN";
}
