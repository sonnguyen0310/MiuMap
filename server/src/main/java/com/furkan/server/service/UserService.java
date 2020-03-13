package com.furkan.server.service;

import com.furkan.server.model.ResponseData;
import com.furkan.server.model.User;
import com.furkan.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseData register(User user, BindingResult bindingResult);
    User save(User user);
    ResponseData login(Map<String, String> json);
    ResponseData getUsers();
    ResponseData updateProfile(Map<String, String> json);
    ResponseData returnInvalidUser(BindingResult bindingResult);

}
