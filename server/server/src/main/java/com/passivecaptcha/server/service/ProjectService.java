package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.AppUser;
import com.passivecaptcha.server.model.Project;
import com.passivecaptcha.server.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(String name, String domain, AppUser owner) {
        Project project = new Project();
        project.setName(name);
        project.setDomain(domain);
        project.setOwner(owner);
        project.setApiKey("pc_live_" + UUID.randomUUID().toString().replace("-", ""));
        return projectRepository.save(project);
    }

    public Project getProjectByApiKey(String apiKey) {
        return projectRepository.findByApiKey(apiKey)
                .orElse(null);
    }
}
