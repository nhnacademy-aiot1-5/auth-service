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

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/login");
    }

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String jwtToken = jwtEncoder.createJwt(authResult.getName());
        LoginResponseDto loginResponseDto = new LoginResponseDto("Bearer", jwtToken);
        String resp = objectMapper.writeValueAsString(loginResponseDto);
        response.getOutputStream().print(resp);
    }
}
