package com.example.social_media.user;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoService {
   private static List<User> users = new ArrayList<>();

   static {
       users.add(new User(1,"Adam", LocalDateTime.now().minusYears(30)));
       users.add(new User(2,"Eve", LocalDateTime.now().minusYears(25)));
       users.add(new User(3,"Jim", LocalDateTime.now().minusYears(20)));
   }

   public List<User> findAll() {
       return users;
   }

}
