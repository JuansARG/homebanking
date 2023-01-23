package com.mindhub.homebanking.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class WebAuthorization {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .antMatchers("/web/login.html", "/assets/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
//                .antMatchers("/manager.html", "/h2-console", "/rest/**").hasAuthority("ADMIN")
//                .antMatchers("/web/**", "/api/clients/current").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.GET, "/api/loans").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.PUT, "/api/loans").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.DELETE, "api/clients/current/accounts/**").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/clients/current/cards", "/api/transactions", "/api/loans").hasAuthority("CLIENT")
//                .anyRequest().denyAll();

        //AGREGAR LAS URL DE LOS NUEVOS END POINTS

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exception) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        //MIRAR METODOS DE LA RES

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).invalidateHttpSession(true).clearAuthentication(true);

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout()
                .logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
