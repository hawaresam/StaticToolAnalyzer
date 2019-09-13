package com.philips.staticanalysis.gatingapp.web;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.philips.staticanalysis.gatingapp.GatingAppApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatingAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckerToolRESTControllerTest {
	
	@LocalServerPort
	private int port;
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	
	@Test
	public void addNewProjectOfNewUserReturnsNOGO() {
		HttpEntity<String> entity = new HttpEntity<String>("https://github.com/fastfoodcoding/SpringBootMongoDbCRUD.git", headers);
		ResponseEntity<Integer> response = restTemplate.exchange(
				createURLWithPort("/api/checkertools/newuser/newproject/filepathofgit"),
				HttpMethod.POST, entity, Integer.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		System.out.println("SDFGHJ: "+actual);
		assertTrue(actual.contains("/api/checkertools/project/nogo"));
	}
	
	@Test
	public void addLegacyProjectOfNewUserMustReturnGO() {
		HttpEntity<String> entity = new HttpEntity<String>("https://github.com/fastfoodcoding/SpringBootMongoDbCRUD.git", headers);
		ResponseEntity<Integer> response = restTemplate.exchange(
				createURLWithPort("/api/checkertools/newuser/legacyproject/filepathofgit"),
				HttpMethod.POST, entity, Integer.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		System.out.println("SDFGHJ: "+actual);
		assertTrue(actual.contains("/api/checkertools/project/go"));
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
	@Test
	public void addProjectOfExistingUserMustReturnGO() {
		HttpEntity<String> entity = new HttpEntity<String>("https://github.com/fastfoodcoding/SpringBootMongoDbCRUD.git", headers);
		ResponseEntity<Integer> response = restTemplate.exchange(
				createURLWithPort("/api/checkertools/existinguser/oldproject/files/97"),
				HttpMethod.POST, entity, Integer.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		System.out.println("SDFGHJ: "+actual);
		assertTrue(actual.contains("/api/checkertools/project/go"));
	}
//	
	@Test
	public void getProjectReportReturnEmpty() {
		HttpEntity<String[]> entity = new HttpEntity<String[]>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/checkertools/project/staticanalysisreport/reports/97"),
				HttpMethod.GET, entity, String.class);
		
		boolean status=false;
		if(response.getBody().equals("[]"))
			status=true;
		assertTrue(status);
	}
	
//	@Test
//	public void getProjectReportForASpecificFile() {
//		HttpEntity<String> entity = new HttpEntity<String>("start.java", headers);
//		ResponseEntity<String> response = restTemplate.exchange(
//				createURLWithPort("/api/checkertools/project/staticanalysisreport/detailedreports/97"),
//				HttpMethod.GET, entity, String.class);
//		
//		boolean status=false;
//		
//		System.out.println(response.getBody());
//		if(response.getBody().equals("[]"))
//			status=true;
////		assertTrue(status);
//	}
}
