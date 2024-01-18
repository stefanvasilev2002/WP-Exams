package mk.ukim.finki.wp.june2022.g1.config;

import mk.ukim.finki.wp.june2022.g1.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is used to configure user login on path '/login' and logout on path '/logout'.
 * The only public page in the application should be '/'.
 * All other pages should be visible only for the authenticated users.
 * Furthermore, in the "list.html" template, the 'Edit', 'Delete', 'Add' buttons should only be
 * visible for a user with role 'ROLE_SYSADMIN'.
 * <p>
 * For login the users from the database should be used.
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   /* @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    LoginService loginService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder);
    }*/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2**"); // do not remove this line
        // TODO: If you are implementing the security requirements, remove this following line
        web.ignoring().antMatchers("/**");
    }
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/","/login").permitAll()
                .antMatchers("/VirtualServers").hasAnyRole("SYSADMIN", "USER")
                .antMatchers("/VirtualServers/**").hasRole("SYSADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
    }*/

}
