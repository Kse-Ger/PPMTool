package com.kseger.ppmtool.web;

import com.kseger.ppmtool.domain.Project;
import com.kseger.ppmtool.services.MapValidationErrorService;
import com.kseger.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId){
        Project project = projectService.findProjectByIdentifier(projectId);
        return new ResponseEntity<Project>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(){return projectService.findAllProjects();}

    @DeleteMapping("/delete{projectid}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectid){
        projectService.deleteProjectByIdentifier(projectid);
        return new ResponseEntity<String>("Project with ID '"+projectid+"' was deleted", HttpStatus.OK);
    }

    @PostMapping("/update{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable String projectId, @Valid @RequestBody Project project, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        projectService.updateProject(project, projectId);
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }
}
