package projectjava.belajar.resfullapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectjava.belajar.resfullapi.model.RegisterUserRequest;

import java.util.Set;

@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public void validation(Object request){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
