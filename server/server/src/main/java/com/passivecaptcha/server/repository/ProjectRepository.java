package com.passivecaptcha.server.repository;

import com.passivecaptcha.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByApiKey(String apiKey);
}
