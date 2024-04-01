package live.ioteatime.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.authservice.domain.LoginRequestDto;
import live.ioteatime.authservice.domain.LoginResponseDto;
import live.ioteatime.authservice.exception.AuthenticationFailedException;
import live.ioteatime.authservice.jwt.JwtEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 요청을 가로채 사용자 인증을 수행하고, 인증 성공 시 JWT 토큰을 생성하여 반환하는 필터.
 * {@inheritDoc}
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    /**
     * JwtAuthenticationFilter의 생성자입니다. 의존성 주입을 통해 필요한 컴포넌트를 초기화합니다.
     * @param jwtEncoder JWT 토큰 생성을 담당하는 컴포넌트
     * @param authenticationManager 사용자 인증을 담당하는 Spring Security의 AuthenticationManager
     * @param objectMapper HTTP 요청의 본문을 Java 객체로 변환하기 위한 ObjectMapper
     */
    public JwtAuthenticationFilter(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/login");
    }

    /**
     * 로그인 요청을 처리하고 사용자 인증을 시도합니다.
     *
     * @param request 로그인 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 로그인 응답을 보낼 HttpServletResponse 객체
     * @return 사용자 인증에 성공했을 경우, 생성된 Authentication 객체
     * @throws AuthenticationException 사용자 인증에 실패하거나 인증 과정에서 예외가 발생한 경우
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getId(),
                    loginRequestDto.getPw()
            );
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }

    /**
     * 사용자 인증에 성공한 후, JWT 토큰을 생성하고 응답에 포함시킵니다.
     *
     * @param request 로그인 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response JWT 토큰을 포함시켜 응답할 HttpServletResponse 객체
     * @param chain 필터 체인을 통해 다음 필터로 요청을 전달할 수 있도록 하는 FilterChain 객체
     * @param authResult 인증 과정에서 생성된 Authentication 객체
     * @throws IOException 응답에 JWT 토큰을 포함시키는 과정에서 오류가 발생한 경우
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String jwtToken = jwtEncoder.createJwt(authResult.getName());
        LoginResponseDto loginResponseDto = new LoginResponseDto("Bearer", jwtToken);
        String resp = objectMapper.writeValueAsString(loginResponseDto);
        response.getOutputStream().print(resp);
    }
}
