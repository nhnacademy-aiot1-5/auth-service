package live.ioteatime.authservice.adaptor;

import live.ioteatime.authservice.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Api 서버에 유저 정보 요청 Adapter
 */
@FeignClient(value = "api-service", path = "/users")
public interface UserServiceAdaptor {
    /**
     * api 서버에 userId에 맞는 유저 가져온다.
     * @param id 주소값의 아이디값
     * @return 유저아이디에 맞는 유저 id,pw가 User 매핑되어 리턴
     */
    @GetMapping("/{userId}/details")
    Optional<User> getUser(@PathVariable(name = "userId") String id);
}
