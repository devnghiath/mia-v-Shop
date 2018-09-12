package thn.vn.web.miav.shop.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.models.entity.TokenApp;
import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.services.UserDetailsServiceImpl;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    protected Log logger = LogFactory.getLog(this.getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private UserDetailsServiceImpl customUserDetailsService;
    @Autowired
    private DataBaseService dataBaseService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        Collection< ? extends GrantedAuthority> authorities = authentication.getAuthorities();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(authentication);
        UserApp userApp = dataBaseService.find(UserApp.class,"email=? or userName=?",new ParameterSql[]{new ParameterSql(String.class,userDetails.getUsername()),new ParameterSql(String.class,userDetails.getUsername())});
        String tokenId =Utils.getMD5EncryptedValue(userApp.getId()+ Utils.DateNow(Utils.DATE_FILE_NOW));
        TokenApp tokenApp = new TokenApp();
        tokenApp.setTokenValue(jwt);
        tokenApp.setTokenType(0);
        tokenApp.setTokenId(tokenId);
        tokenApp.setUserId(userApp.getId());
        dataBaseService.save(tokenApp);

        Cookie authenticationCookie = new Cookie("tokenId", tokenId);
        authenticationCookie.setPath("/");
        authenticationCookie.setHttpOnly(true);
        authenticationCookie.setSecure(request.isSecure());
        authenticationCookie.setMaxAge(3600000);
        response.addCookie(authenticationCookie);
        authorities.forEach(authority->{
            String url = request.getParameter("redirect");
            if(authority.getAuthority().equals("ROLE_USER")) {
                if (Utils.isEmpty(url)){
                    url = "/";
                }
                try {

                    redirectStrategy.sendRedirect(request, response, url);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equals("ROLE_ADMIN")) {

                if (Utils.isEmpty(url)){
                    url = "/admin";
                }
                try {
                    redirectStrategy.sendRedirect(request, response, url);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                throw new IllegalStateException();
            }
        });
        clearAuthenticationAttributes(request);

    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;

        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            return "/";
        } else if (isAdmin) {
            return "/admin";
        } else {
            throw new IllegalStateException();
        }
    }




    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }


    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
