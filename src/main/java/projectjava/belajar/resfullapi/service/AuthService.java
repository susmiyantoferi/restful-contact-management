package projectjava.belajar.resfullapi.service;

import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import projectjava.belajar.resfullapi.entity.User;
import projectjava.belajar.resfullapi.model.LoginUserRequest;
import projectjava.belajar.resfullapi.model.TokenResponse;
import projectjava.belajar.resfullapi.repository.UserRepository;
import projectjava.belajar.resfullapi.security.BCrypt;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validation(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            //sukses
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(tokenExpired());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }


    }

    private Long tokenExpired() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24);
    }

}
