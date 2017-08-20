package com.dyachenko.springsecurityapp.dao;

import com.dyachenko.springsecurityapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Id-шник у нас это лонговое значение, поэтому лон
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
