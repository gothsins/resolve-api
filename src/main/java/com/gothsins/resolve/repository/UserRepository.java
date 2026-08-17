package com.gothsins.resolve.repository;

import com.gothsins.resolve.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
