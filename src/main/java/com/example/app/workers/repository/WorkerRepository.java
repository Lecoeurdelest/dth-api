package com.example.app.workers.repository;

import com.example.app.auth.domain.Role;
import com.example.app.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<User, Long> {

    // Find all workers
    List<User> findByRole(Role role);

    // Find workers by skills containing a keyword
    List<User> findByRoleAndSkillsContaining(Role role, String skillFragment);

    // Find worker by ID and role (to ensure it's a worker)
    Optional<User> findByIdAndRole(Long id, Role role);

    // Custom query to find workers with skills containing a specific skill
    @Query("SELECT u FROM User u WHERE u.role = :role AND " +
           "(u.skills LIKE CONCAT('%', :skill, '%') OR " +
           "JSON_SEARCH(u.skills, 'one', :skill) IS NOT NULL)")
    List<User> findWorkersBySkill(@Param("role") Role role, @Param("skill") String skill);
}
