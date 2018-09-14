package thn.vn.web.miav.shop.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        String url_filter = httpServletRequest.getRequestURI();
        if (!url_filter.startsWith("/api")&&!url_filter.startsWith("/auth")){
            httpServletResponse.sendRedirect("/sign?redirect="+url_filter);
        } else {
            httpServletResponse.sendRedirect("/error/api");
//            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//                    "Sorry, You're not authorized to access this resource.");
        }
    }
}