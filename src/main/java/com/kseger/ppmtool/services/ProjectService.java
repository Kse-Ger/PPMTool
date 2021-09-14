package com.kseger.ppmtool.services;

import com.kseger.ppmtool.domain.Project;
import com.kseger.ppmtool.exceptions.ProjectIdException;
import com.kseger.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            project.setUpdated_At(null);
            project.onCreate();
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists.");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' doesn't exist.");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectid){
        Project project = projectRepository.findByProjectIdentifier(projectid.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Cannot delete project with ID '"+projectid+"'. This project doesn't exist.");
        }
        projectRepository.delete(project);
    }

    public Project updateProject(Project project, String projectId){

        Project projectDefault = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (projectDefault == null){
            throw new ProjectIdException("Cannot find project with ID '"+projectId+"'.");
        }
        project.setProjectIdentifier(projectId.toUpperCase());
        project.setCreated_At(projectDefault.getCreated_At());
        project.onUpdate();
        projectRepository.delete(projectDefault);

        return projectRepository.save(project);
    }
}
