package com.example.app.auth.repository;

import com.example.app.auth.domain.Role;
import com.example.app.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByPhone(String phone);
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByFacebookId(String facebookId);

    // Find workers by role and skills containing a keyword (simple text match)
    java.util.List<User> findByRoleAndSkillsContaining(Role role, String skillFragment);
    
    // Count users by role
    Long countByRole(Role role);
}




