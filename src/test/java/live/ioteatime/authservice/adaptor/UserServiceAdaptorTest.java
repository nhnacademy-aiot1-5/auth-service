package live.ioteatime.authservice.adaptor;

import live.ioteatime.authservice.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceAdaptorTest {
    @Mock
    private UserServiceAdaptor userServiceAdaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // 목 객체 초기화
    }
    @Test
    void getUser() {
        String userId = "testId";
        User user = new User(userId, "pw");

        when(userServiceAdaptor.getUser(userId)).thenReturn(Optional.of(user));

        Optional<User> actual = userServiceAdaptor.getUser(userId);
        assertTrue(actual.isPresent());
        User response = actual.get();
        assertTrue(response.getId().equals(userId));

        verify(userServiceAdaptor).getUser(userId);
    }


}