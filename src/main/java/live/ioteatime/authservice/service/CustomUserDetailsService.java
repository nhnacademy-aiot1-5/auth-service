package live.ioteatime.authservice.service;

import live.ioteatime.authservice.adaptor.UserServiceAdaptor;
import live.ioteatime.authservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

/**
 * AuthenticationProvider 가 인증처리를 위해 사용하는 UserDetailsService
 */
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserServiceAdaptor userServiceAdaptor;

    /**
     *
     * @param username  api 서버에 유저 정보를 요청하기위한 파라미터
     * @return UserDetails 의 User 객체
     * @throws UsernameNotFoundException 이름과 일치한 사용자가 없을때 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userServiceAdaptor.getUser(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getId(), user.getPw(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
