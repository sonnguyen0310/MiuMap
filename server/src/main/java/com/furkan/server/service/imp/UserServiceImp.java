package com.furkan.server.service.imp;

import com.furkan.server.model.ResponseData;
import com.furkan.server.model.Status;
import com.furkan.server.model.User;
import com.furkan.server.repository.UserRepository;
import com.furkan.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseData register(User user, BindingResult bindingResult) {
        ResponseData responseData = new ResponseData();
        if (bindingResult.hasErrors()) {
            return returnInvalidUser(bindingResult);
        }

        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            responseData.setStatus(Status.NEGATIVE);
            responseData.setMessage("Email is already taken.");
        } else {
            save(user);
            responseData.setData(user);
            responseData.setStatus(Status.POSITIVE);
            responseData.setMessage("User is registered successfully.");
        }
        return responseData;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseData<User> login(Map<String, String> json) {
        ResponseData<User> responseData = new ResponseData<>();
        User user = userRepository.findUserByEmail(json.get("email"));
        if (user == null) {
            responseData.setStatus(Status.NEGATIVE);
            responseData.setMessage("Wrong email address");
        } else {
            if (user.getPassword().equals(json.get("password"))) {
                responseData.setStatus(Status.POSITIVE);
                responseData.setData(user);
                responseData.setMessage("User is logged in successfully.");
            } else {
                responseData.setStatus(Status.NEGATIVE);
                responseData.setMessage("Wrong password");
            }
        }
        return responseData;
    }

    @Override
    public ResponseData<List<User>> getUsers() {
        ResponseData responseData = new ResponseData();
        List<User> userList = userRepository.findAll();
        if (userList.size() < 1) {
            responseData.setStatus(Status.NEGATIVE);
            responseData.setMessage("User list is empty.");
        } else responseData.setStatus(Status.POSITIVE);
        responseData.setData(userList);
        responseData.setMessage("Users listed.");
        return responseData;
    }

    @Override
    public ResponseData updateProfile(Map<String, String> json) {
        ResponseData responseData = new ResponseData();
        User userFromDatabase = userRepository.findUserByEmail((json.get("oldEmail")));
        if (userFromDatabase == null) {
            responseData.setStatus(Status.ERROR);
            responseData.setMessage("Incorrect email.");
            return responseData;
        }
        if (!(json.get("password").equals(userFromDatabase.getPassword()))) {
            responseData.setStatus(Status.ERROR);
            responseData.setMessage("Incorrect password.");
            return responseData;
        }

        if (((json.get("email"))).trim().isEmpty() ||
                ((json.get("firstName"))).trim().isEmpty() ||
                ((json.get("lastName"))).trim().isEmpty() ||
                ((json.get("phone"))).trim().isEmpty()) {
            responseData.setStatus(Status.NEGATIVE);
            responseData.setMessage("All fields should be updated.");
            return responseData;
        }

        User updatedUser = new User(json.get("firstName"), json.get("lastName"), json.get("email"), userFromDatabase.getPassword(), json.get("phone"));
        updatedUser.setId(userFromDatabase.getId());
        userRepository.save(updatedUser);

        responseData.setStatus(Status.POSITIVE);
        responseData.setData(updatedUser);
        responseData.setMessage("User is updated successfully.");
        return responseData;
    }



    @Override
    public ResponseData returnInvalidUser(BindingResult bindingResult) {
        ResponseData responseData = new ResponseData();
        StringBuilder builder = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            builder.append(error.getField() + " : " + error.getDefaultMessage());
        }

        responseData.setStatus(Status.NEGATIVE);
        responseData.setMessage(builder.toString());
        return responseData;
    }
}
