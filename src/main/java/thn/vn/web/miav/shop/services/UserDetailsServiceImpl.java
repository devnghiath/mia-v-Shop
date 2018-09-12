package thn.vn.web.miav.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thn.vn.web.miav.shop.dao.DataBaseDao;
import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.models.entity.UserPrincipal;
import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private DataBaseDao userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserApp user = userRepository.find(UserApp.class,"email=? or userName=?",new ParameterSql[]{new ParameterSql(String.class,s),new ParameterSql(String.class,s)});

        Set<GrantedAuthority> grantedAuthorities = new HashSet();
//        for (Role role : user.getRoles()){
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
        if (user.getRole() == 0){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else  {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        return new UserPrincipal(user.getId(),user.getUserName(),user.getUserName(),user.getEmail(), user.getPassWord(), grantedAuthorities);
    }
    public UserDetails loadUserId(int id) throws UsernameNotFoundException {
        UserApp user = userRepository.find(UserApp.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,id)});

        Set<GrantedAuthority> grantedAuthorities = new HashSet();
//        for (Role role : user.getRoles()){
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
        if (user.getRole() == 0){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else  {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        return new UserPrincipal(user.getId(),user.getUserName(),user.getUserName(),user.getEmail(), user.getPassWord(), grantedAuthorities);
    }
}