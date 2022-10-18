package bssm.db.bssmgit.global.util;

import bssm.db.bssmgit.global.auth.CustomUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static CustomUserDetail getCurrentUser() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}

