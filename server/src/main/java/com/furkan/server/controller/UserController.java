package com.furkan.server.controller;

import com.furkan.server.model.ResponseData;
import com.furkan.server.model.Status;
import com.furkan.server.model.User;
import com.furkan.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseData register(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.register(user, bindingResult);
    }

    @PostMapping("/login")
    public ResponseData login(@RequestBody Map<String, String> json) {
        return userService.login(json);
    }

    @GetMapping("/list")
    public ResponseData list() {
        return userService.getUsers();
    }

    @PostMapping("/update")
    public ResponseData update(@RequestBody Map<String, String> json) {
        return userService.updateProfile(json);
    }

}
