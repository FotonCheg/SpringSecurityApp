package com.dyachenko.springsecurityapp.dao;

import com.dyachenko.springsecurityapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
/**
* Этот Интерфейс является Dao'шным интерфейсом для такой сущности, как Role, так же нам нужен UserDao
*/
public interface RoleDao extends JpaRepository<Role, Long>{
}
