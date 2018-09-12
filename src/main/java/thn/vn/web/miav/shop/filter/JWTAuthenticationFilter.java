package thn.vn.web.miav.shop.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.models.entity.TokenApp;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.services.UserDetailsServiceImpl;
import thn.vn.web.miav.shop.utils.ParameterSql;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserDetailsServiceImpl customUserDetailsService;
    @Autowired
    private DataBaseService dataBaseService;
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String url_filter = request.getRequestURI();
            String jwt = getJwtFromRequest(request);
            TokenApp tokenApp = dataBaseService.find(TokenApp.class,"tokenId=?",new ParameterSql[]{new ParameterSql(String.class,jwt)});
            //tokenId
            if (tokenApp!=null){
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(tokenApp.getTokenValue())) {
                    int userId = tokenProvider.getUserIdFromJWT(tokenApp.getTokenValue());
                    UserDetails userDetails = customUserDetailsService.loadUserId(userId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwtNew = tokenProvider.generateToken(authentication);
                    tokenApp.setTokenValue(jwtNew);
                    dataBaseService.save(tokenApp);
                    if (!url_filter.startsWith("/api")){
                        Cookie authenticationCookie = new Cookie("tokenId", tokenApp.getTokenId());
                        authenticationCookie.setPath("/");
                        authenticationCookie.setHttpOnly(true);
                        authenticationCookie.setSecure(request.isSecure());
                        authenticationCookie.setMaxAge(3600000);
                        response.addCookie(authenticationCookie);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String url_filter = request.getRequestURI();
        String bearerToken = request.getHeader("Authorization");
        if (!url_filter.startsWith("/api")){
            for(Cookie cookie:request.getCookies()){
                if (cookie.getName().equals("tokenId")){
                    bearerToken = cookie.getValue();
                    break;
                }
            }
            return bearerToken;
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}

