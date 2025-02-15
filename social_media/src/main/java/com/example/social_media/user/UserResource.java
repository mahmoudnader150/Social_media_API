package com.example.social_media.user;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserResource {
    private UserDaoService userDaoService;

    public UserResource(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    public List<User> retrieveAllUsers(){
          return userDaoService.findAll();
    }
}
