package ru.tigran.web_editor.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ru.tigran.web_editor.database.service.UserService


@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    @Autowired
    lateinit var userService: UserService

    @Bean
    fun bCryptPasswordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.headers().frameOptions().sameOrigin()
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/registration").not().fullyAuthenticated()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
            .antMatchers("/resources/**", "/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .successHandler { _, response, authentication ->
                response.sendRedirect(determineTargetUrl(authentication))
            }
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .logoutSuccessUrl("/")
        return http.build()
    }

    @Bean
    fun userDetailsService() : UserDetailsService{
        return userService
    }

    private fun determineTargetUrl(authentication: Authentication) : String {
        val authorities = authentication.authorities
        for (authority in authorities) {
            if (authority.authority.equals("ROLE_ADMIN")) return "/admin"
        }
        return "/"
    }
}