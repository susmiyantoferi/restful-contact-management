package projectjava.belajar.resfullapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import projectjava.belajar.resfullapi.entity.User;
import projectjava.belajar.resfullapi.model.RegisterUserRequest;
import projectjava.belajar.resfullapi.model.UpdateUserRequest;
import projectjava.belajar.resfullapi.model.UserResponse;
import projectjava.belajar.resfullapi.repository.UserRepository;
import projectjava.belajar.resfullapi.security.BCrypt;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validation(request);

        if (userRepository.existsById(request.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request){
        validationService.validation(request);
        log.info("REQUEST : {}", request);

        if (Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }
        userRepository.save(user);
        log.info("USER : {}", user.getName());

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
