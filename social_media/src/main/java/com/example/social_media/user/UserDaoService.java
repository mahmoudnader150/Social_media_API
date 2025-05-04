package com.example.social_media.user;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {
   private static List<User> users = new ArrayList<>();

   private static long usersCount = 0;

   static {
       users.add(new User(++usersCount, "Adam", LocalDateTime.now().minusYears(30), "adam@example.com", "$2a$10$Xt5wqIy.XbH1b5GFrxXyVeV6gMkgM9M5yLhJh/JYuP3S6OYTpV6Vy")); // password: password123
       users.add(new User(++usersCount, "Eve", LocalDateTime.now().minusYears(25), "eve@example.com", "$2a$10$Xt5wqIy.XbH1b5GFrxXyVeV6gMkgM9M5yLhJh/JYuP3S6OYTpV6Vy")); // password: password123
       users.add(new User(++usersCount, "Jim", LocalDateTime.now().minusYears(20), "jim@example.com", "$2a$10$Xt5wqIy.XbH1b5GFrxXyVeV6gMkgM9M5yLhJh/JYuP3S6OYTpV6Vy")); // password: password123
   }

   public List<User> findAll() {
       return users;
   }

   public User findOne(Long id) {
       Predicate<? super User> predicate = user -> user.getId().equals(id);
       return users.stream().filter(predicate).findFirst().orElse(null);
   }

   public User findByEmail(String email) {
       Predicate<? super User> predicate = user -> user.getEmail() != null && user.getEmail().equals(email);
       return users.stream().filter(predicate).findFirst().orElse(null);
   }

   public void deleteById(Long id) {
       Predicate<? super User> predicate = user -> user.getId().equals(id);
       users.removeIf(predicate);
   }

   public User save(User user) {
       // Check if email already exists
       if (findByEmail(user.getEmail()) != null) {
           throw new RuntimeException("Email already registered");
       }
       user.setId(++usersCount);
       users.add(user);
       return user;
   }
}
