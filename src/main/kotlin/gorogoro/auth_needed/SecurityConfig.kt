package gorogoro.auth_needed

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authz ->
            authz
                //.requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                .anyRequest().authenticated()
        }

        http.sessionManagement { sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.oauth2ResourceServer { oauth2 ->
            oauth2.jwt { jwt ->
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            }
        }

        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withJwkSetUri("https://localhost:18443/realms/jxhui/protocol/openid-connect/certs")
            .build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter(CustomJwtGrantedAuthoritiesConverter())
        return converter
    }

    /**
     * Parses the JWT string and fetches the roles out of it
     */
    class CustomJwtGrantedAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {
        override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
            val authorities = mutableListOf<GrantedAuthority>()
            val roles = jwt.getClaim<List<String>>("roles")
            roles?.forEach { role ->
                authorities.add(SimpleGrantedAuthority("ROLE_$role"))
            }
            return authorities
        }
    }
}
