package me.imsergioh.livecore.controller;

import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return UserService.get().getUsers();
    }
}