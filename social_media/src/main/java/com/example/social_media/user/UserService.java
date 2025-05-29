package com.example.social_media.user;

import com.example.social_media.user.dto.UserProfileDTO;
import com.example.social_media.user.dto.UserResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public User save(User user) {
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> searchByName(String searchTerm) {
        return userRepository.searchByName(searchTerm);
    }

    public void updateUserStatus(Long userId, boolean isOnline) {
        User user = findById(userId);
        user.setOnline(isOnline);
        userRepository.save(user);
    }

    public void updateLastSeen(Long userId) {
        User user = findById(userId);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Get comprehensive user profile data including followers, following, posts, likes, etc.
     * 
     * @param userId The ID of the user
     * @param includeFollowers Whether to include full follower data
     * @param includeFollowing Whether to include full following data
     * @return UserProfileDTO containing all requested user data
     */
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Long userId, boolean includeFollowers, boolean includeFollowing) {
        User user = findById(userId);
        return new UserProfileDTO(user, includeFollowers, includeFollowing);
    }

    public UserResponse updateProfile(User currentUser, com.example.social_media.user.dto.UpdateProfileRequest request) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            currentUser.setName(request.getName().trim());
        }
        if (request.getBirthDate() != null) {
            currentUser.setBirthDate(request.getBirthDate());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User updatedUser = userRepository.save(currentUser);
        return new UserResponse(updatedUser);
    }
}