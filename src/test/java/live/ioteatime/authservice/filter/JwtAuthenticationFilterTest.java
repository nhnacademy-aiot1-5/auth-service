package live.ioteatime.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.authservice.domain.LoginRequestDto;
import live.ioteatime.authservice.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(JwtUtil.class)
@Slf4j
class JwtAuthenticationFilterTest {



    @Mock
    private AuthenticationManager authenticationManager;


    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void attemptAuthentication() throws IOException {
        String id = "testId";
        String pw = "testPw";
        ObjectMapper mapper = new ObjectMapper();
        jwtAuthenticationFilter = new JwtAuthenticationFilter(new JwtUtil(), authenticationManager, mapper);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setId(id);
        loginRequestDto.setPw(pw);
        String s = mapper.writeValueAsString(loginRequestDto);
        InputStream inputStream = new ByteArrayInputStream(s.getBytes());

        given(request.getInputStream()).willReturn(new DelegatingServletInputStream(inputStream));
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "test");
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);

    }

    @Test
    void successfulAuthentication() {
    }
}