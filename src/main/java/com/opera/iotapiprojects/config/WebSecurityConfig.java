package com.opera.iotapiprojects.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String[] PUBLIC_URLS = {"/actuator/**", "/swagger-ui.html", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/v2/api-docs"};
  

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .cors().and()
      .csrf().disable()
      .authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
      .anyRequest().authenticated().and()
      .oauth2ResourceServer().jwt();
    // @formatter:on
  }
}
