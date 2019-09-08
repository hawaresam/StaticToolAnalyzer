package com.philips.staticanalysis.gatingapp.web;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.philips.staticanalysis.gatingapp.service.CheckerToolService;

@RestController
public class CheckerToolRESTController {
	CheckerToolService cservice;
	static final String GO="/api/checkertools/project/go";
	static final String NOGO="/api/checkertools/project/nogo";
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CheckerToolRESTController.class);

	@Autowired
	public void setCservice(CheckerToolService cservice) {
		this.cservice = cservice;
	}
	
	@PostMapping(value = "/api/checkertools/newuser/newproject/filepathofgit")
	public ResponseEntity<Object> addNewProjectOfNewUser(@RequestBody String pathOfGitRepo) throws InterruptedException{
		try {
			String status= cservice.newProjectOfNewUserExecute(pathOfGitRepo);
			log.info(status);
			
			HttpHeaders headers=new HttpHeaders();
			if(status.equals("GO")) {
				headers.setLocation(URI.create(GO));
			}
			else {
				headers.setLocation(URI.create(NOGO));
			}
			return new ResponseEntity<>(headers,HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/api/checkertools/newuser/legacyproject/filepathofgit")
	public ResponseEntity<Object> addLegacyProjectOfNewUser(@RequestBody String pathOfGitRepo) throws InterruptedException{
		String status= cservice.legacyProjectOfNewUserExecute(pathOfGitRepo);

		HttpHeaders headers=new HttpHeaders();
		if(status.equals("GO")) {
			headers.setLocation(URI.create(GO));
		}
		else {
			headers.setLocation(URI.create(NOGO));
		}
		return new ResponseEntity<>(headers,HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/api/checkertools/existinguser/oldproject/files/{id}")
	public ResponseEntity<Object> addProjectOfExistingUser(@RequestBody String pathOfGitRepo,@PathVariable("id")int pid) throws InterruptedException{
		String status= cservice.projectOfExistingUserExecute(pathOfGitRepo,pid);

		HttpHeaders headers=new HttpHeaders();
		if(status.equals("GO")) {
			headers.setLocation(URI.create(GO));
		}
		else {
			headers.setLocation(URI.create(NOGO));
		}
		return new ResponseEntity<>(headers,HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/api/checkertools/project/staticanalysisreport/reports/{id}")
	public ResponseEntity<List<String>> getProjectReport(@PathVariable("id")int pid){
		List<String> listOfJavaFiles=cservice.getJavaFilesPresentInInputFolder();
		return new ResponseEntity<>(listOfJavaFiles,HttpStatus.OK);
	}
	
	@GetMapping(value = "/api/checkertools/project/staticanalysisreport/detailedreports/{id}")
	public ResponseEntity<List<String>> getProjectReportForASpecificFile(@PathVariable("id")int pid, @RequestBody String filename){
		List<String> listOfErrors=cservice.getErrorsFromAllTools(filename);
		return new ResponseEntity<>(listOfErrors,HttpStatus.OK);
	}
}
