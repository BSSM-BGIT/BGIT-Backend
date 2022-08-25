package bssm.db.bssmgit.global.jwt.exception;

import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.global.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (CustomException e) {
            setErrorResponse(response, e);
        }
    }

    public void setErrorResponse(HttpServletResponse res, CustomException e) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json; charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        res.getWriter().write(errorResponse.convertToJson());
    }
}
