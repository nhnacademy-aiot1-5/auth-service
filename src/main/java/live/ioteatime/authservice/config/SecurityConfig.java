package live.ioteatime.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.authservice.adaptor.UserServiceAdaptor;
import live.ioteatime.authservice.filter.JwtAuthenticationFilter;
import live.ioteatime.authservice.jwt.JwtEncoder;
import live.ioteatime.authservice.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티 구성을 정의하는 설정 클래스입니다.
 * 이 클래스는 애플리케이션의 보안 관련 구성을 설정합니다.
 * 필요한 빈들을 정의 하여 스프링 컨테이너에 등록합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserServiceAdaptor userServiceAdaptor;

    /**
     * 스프링 시큐리티 필터체인을 정의합니다.
     * csrf보호 기능을 해제하고, 세션을 stateless 로 설정합니다.
     * 세션 정책 설정, 폼 기반 로그인 비활성화, 기본 HTTP 인증 비활성화 및 JWT 인증 필터를 추가 했습니다.
     *
     * @param http http HttpSecurity 객체를 사용하여 보안 구성을 정의합니다.
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 필터 체인 구성 중 발생하는 예외를 처리합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterAt(new JwtAuthenticationFilter(jwtUtil(),
                        authenticationManager(null), objectMapper()), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll();

        return http.build();
    }

    /**
     * ObjectMapper 빈을 생성 합니다.
     * 주로 HTTP 요청의 본문을 Java 객체로 직렬화 혹은 역직렬화하는데 사용 됩니다.
     *
     * @return 새로운 ObjectMapper 객체
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * 스프링 시큐리티의 AuthenticationManager를 구성합니다.
     * 이 메서드는 AuthenticationManager 를 CustomeUserDetailsService 인증 방식과 연결 하는 데 사용됩니다.
     *
     * @param authenticationConfiguration 기본 인증 매니저를 가져옵니다.
     * @return 구성된 authenticationManger 인스턴스
     * @throws Exception 인증 매니저 구성중 발생하는 예외 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 사용자 정의 인증 방식을 구현 한 UserDetailsService 빈을 생성합니다.
     *
     * @return CustomUserDetailsService 인스턴스
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userServiceAdaptor);
    }

    /**
     * jwt token 을 만들고 요청 받은 토큰에 대해 사용자 정보를 제공하는 빈을 생성합니다.
     *
     * @return JwtEncoder 인스턴스
     */
    @Bean
    public JwtEncoder jwtUtil() {
        return new JwtEncoder();
    }

    /**
     * 사용자 비밀번호를 Bcrypt 방식으로 인코딩하는 빈을 생성합니다.
     * 이 컴포넌트는 비밀번호의 해싱 및 검증을 담당합니다.
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
