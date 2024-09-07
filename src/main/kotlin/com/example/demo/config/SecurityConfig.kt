package com.example.demo.config

import com.example.demo.provider.JwtAuthenticationFilter
import com.example.demo.provider.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtTokenProvider: TokenProvider
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll() // Umożliwiamy dostęp bez autoryzacji
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            }.csrf { csrf -> csrf.disable() }

//            .csrf().disable() // Wyłączenie CSRF dla testów; nie zalecane w produkcji bez dodatkowych zabezpieczeń
        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(this.jwtTokenProvider)
    }


}


//@Configuration
//@EnableWebSecurity
//class SecurityConfig(private val jwtTokenProvider: TokenProvider) {
//
//
////
////    override fun configure(http: HttpSecurity) {
////        http
////            .csrf().disable()
////            .authorizeRequests()
////            .antMatchers("/api/auth/**").permitAll()
////            .anyRequest().authenticated()
////            .and()
////            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
////    }
////
////    @Bean
////    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
////        return JwtAuthenticationFilter(jwtTokenProvider)
////    }
//}