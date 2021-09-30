package com.rca.aurhotization.azureb2ctokenvalidator.config;

import com.rca.aurhotization.azureb2ctokenvalidator.authorization.BasicAuthenticationProvider;
import com.rca.aurhotization.azureb2ctokenvalidator.authorization.CredentialsStore;
import com.rca.aurhotization.azureb2ctokenvalidator.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    CredentialsStore credentialsStore;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new BasicAuthenticationProvider(credentialsStore));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (System.getenv(Constants.ENV_BASIC_SECURITY) != null) {
            http.httpBasic().
                    and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                    and().csrf().disable().
                    authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated();
        }
    }
}
