package com.dyachenko.springsecurityapp.dao;

import com.dyachenko.springsecurityapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 2017 on 13.07.2017.
 */
public interface TaskDao extends JpaRepository<Task, Long> {
}
