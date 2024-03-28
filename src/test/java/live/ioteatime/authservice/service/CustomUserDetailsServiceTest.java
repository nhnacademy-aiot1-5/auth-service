package live.ioteatime.authservice.service;

import live.ioteatime.authservice.adaptor.UserServiceAdaptor;
import live.ioteatime.authservice.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Slf4j
@SpringBootTest
class CustomUserDetailsServiceTest {
    @Mock
    private UserServiceAdaptor userServiceAdaptor;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername() {
        User testUser = new User("testUser", "test");

        given(userServiceAdaptor.getUser("testUser")).willReturn(Optional.of(testUser));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(testUser.getId());

        Assertions.assertEquals(userDetails.getUsername(), testUser.getId());
        Assertions.assertEquals(userDetails.getPassword(), testUser.getPw());
        Assertions.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}