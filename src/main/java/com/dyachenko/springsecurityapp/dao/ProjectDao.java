package com.dyachenko.springsecurityapp.dao;

import com.dyachenko.springsecurityapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Created by 2017 on 13.07.2017.
 */
public interface ProjectDao extends JpaRepository<Project, Long> {
}
