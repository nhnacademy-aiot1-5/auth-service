package live.ioteatime.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.authservice.domain.LoginRequestDto;
import live.ioteatime.authservice.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
class JwtAuthenticationFilterTest {
    @Autowired
    private JwtUtil jwtUtil;

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

    private Authentication authentication;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() throws IOException {
        String id = "testId";
        String pw = "testPw";
        mapper = new ObjectMapper();
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, authenticationManager, mapper);
        authentication = new UsernamePasswordAuthenticationToken("test", "test");

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setId(id);
        loginRequestDto.setPw(pw);
        String s = mapper.writeValueAsString(loginRequestDto);
        InputStream inputStream = new ByteArrayInputStream(s.getBytes());

        given(request.getInputStream()).willReturn(new DelegatingServletInputStream(inputStream));
    }

    @Test
    void attemptAuthentication() {

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);
    }

    @Test
    void successfulAuthentication() throws ServletException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // Do nothing
            }
        };

        when(response.getOutputStream()).thenReturn(servletOutputStream);

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authentication);

        String jwtToken = jwtUtil.createJwt(authentication.getName());
        Map<String, String> expectedToken = Map.of("token", jwtToken);
        String expectedResponse = mapper.writeValueAsString(expectedToken);

        assertEquals(expectedResponse, outputStream.toString());
    }
}
