package com.api.finances_backend.repository;

import com.api.finances_backend.entity.User;
import com.api.finances_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Category , Long> {
    List<User> findByUserId(Long userId );
}
