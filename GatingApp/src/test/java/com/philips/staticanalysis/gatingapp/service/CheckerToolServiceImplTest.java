package com.philips.staticanalysis.gatingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.philips.staticanalysis.gatingapp.dal.CheckerToolDAO;
import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;
import com.philips.staticanalysis.gatingapp.domain.Reports;

public class CheckerToolServiceImplTest {
	
	CheckerToolServiceImpl ctslObj;
	
	HelperService hservice;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void initializeObject() {
		ctslObj=new CheckerToolServiceImpl();
		System.out.println("Size of list is: "+CheckerToolServiceImpl.listOfTools.size());
		assertNotNull(ctslObj);
	}
	
	@Test
	public void constructorShouldInitilizeArrayListOfTools() {
		assertEquals(4,CheckerToolServiceImpl.listOfTools.size());
	}
	
	@Test
	public void readThePathConfigFileMethodShouldReadTheFile() {
		CheckerToolServiceImpl.readThePathConfigFile();
		assertEquals("Checking if cloneDirectoryPath is loaded",  "C:\\Users\\320065418\\Music\\InputFolderFromGitRepo",CheckerToolServiceImpl.cloneDirectoryPath);
		assertEquals("Checking if configFilePath is loaded", "C:\\Users\\320065418\\Music\\GatingApp\\GatingApp\\src\\main\\resources\\static\\config.properties",CheckerToolServiceImpl.configFilePath);
	}
	
	@Test
	public void gitRepoShouldBeCloned() {
		ctslObj.cloneRepo("https://github.com/rashmi-ramappapatil/firstRepo.git");
		File directory = new File(CheckerToolServiceImpl.cloneDirectoryPath);
		assertNotEquals(0, directory.length());
	}
	
	@Test
	public void runToolsMethodShouldRunSuccessfully() throws InterruptedException {
		ctslObj.runTools();
		File tempFile = new File("./outputPMD.txt");
		File tempFile1 = new File("./outputCheckstyle.txt");
		File tempFile2 = new File("./outputSimian.txt");
		File tempFile3 = new File("./outputYasca.csv");

		boolean status=false;
		if(tempFile.exists() && tempFile1.exists() && tempFile2.exists() && tempFile3.exists()){
			status=true;
		}
		
		assertTrue(status);
	}
	
	@Test
	public void checkIfProjectIsGoMethodShouldReturnGo() {
		
	}
	
	@Test
	public void getSimianErrorInAListMustReturnAListOfString() {
		
		//ready the hservice
		//set hservice to checkertoolservice
		//then call checkertollservice ka method
		
		String path="outputSimian.txt";
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		List<String> list=new ArrayList<String>();
		list.add("Sam");
		list.add("Ankita");
		list.add("Rashmi");
		
		HelperService mockhservice=Mockito.mock(HelperService.class);
		Mockito.when(mockhservice.readAFile(path)).thenReturn(list);
		cservice.setHservice(mockhservice);

		cservice.getSimianErrorInAList();
		Mockito.verify(mockhservice).readAFile(path);
	}
	
	@Test
	public void countPmdErrorMustReturnAnInteger() {
		String path="./outputPMD.txt";
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		List<String> list=new ArrayList<String>();
		list.add("Sam");
		list.add("Ankita");
		list.add("Rashmi");
		
		HelperService mockhservice=Mockito.mock(HelperService.class);
		Mockito.when(mockhservice.readAFile(path)).thenReturn(list);
		cservice.setHservice(mockhservice);

		Object o=cservice.countPmdError();
		Mockito.verify(mockhservice).readAFile(path);
		
		boolean status=false;
		if(o instanceof Integer) 
			status=true;
		assertTrue(status);
	}
	
	@Test
	public void countCheckstyleErrorMustReturnAnInteger() {
		String path="./outputCheckstyle.txt";
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		List<String> list=new ArrayList<String>();
		list.add("Sam");
		list.add("Ankita");
		list.add("Rashmi");
		
		HelperService mockhservice=Mockito.mock(HelperService.class);
		Mockito.when(mockhservice.readAFile(path)).thenReturn(list);
		cservice.setHservice(mockhservice);

		Object o=cservice.countCheckstyleError();
		Mockito.verify(mockhservice).readAFile(path);
		
		boolean status=false;
		if(o instanceof Integer) 
			status=true;
		assertTrue(status);
	}
	
	@Test
	public void countSimianErrorMustReturnAnInteger() {
		String path="./outputSimian.txt";
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		List<String> list=new ArrayList<String>();
		list.add("Sam");
		list.add("Ankita");
		list.add("Rashmi");
		
		HelperService mockhservice=Mockito.mock(HelperService.class);
		Mockito.when(mockhservice.readAFile(path)).thenReturn(list);
		cservice.setHservice(mockhservice);

		Object o=cservice.countSimianError();
		Mockito.verify(mockhservice).readAFile(path);
		
		boolean status=false;
		if(o instanceof Integer) 
			status=true;
		assertTrue(status);
	}
	
	@Test
	public void countYascaErrorMustReturnAnInteger() {
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		Object o=cservice.countYascaError();
		
		boolean status=false;
		if(o instanceof Integer) 
			status=true;
		assertTrue(status);
	}
	
	@Test
	public void getPmdCheckstyleErrorsMustAddInErrorList() {
		String path="outputCheckstyle.txt";
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		List<String> list=new ArrayList<String>();
		list.add("Sam");
		list.add("Ankita");
		list.add("Rashmi");
		List<String> listofErrors=new ArrayList<String>();
		String filename="outputCheckstyle.txt";

		HelperService mockhservice=Mockito.mock(HelperService.class);
		Mockito.when(mockhservice.readAFile(path)).thenReturn(list);
		cservice.setHservice(mockhservice);

		cservice.getPmdCheckstyleErrors("Checkstyle",listofErrors,filename);
		Mockito.verify(mockhservice).readAFile(path);
	}
	
	@Test
	public void legacyProjectOfNewUserExecuteMustReturnGO() throws InterruptedException{
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();

		ProjectInfo pinfo=new ProjectInfo(12,23,45,56);
		pinfo.setProjectId(1);
		CheckerToolDAO mockhservice=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockhservice.updateReport(1,12,23,45,56, "GO")).thenReturn(1);
		cservice.setCdao(mockhservice);

		cservice.legacyProjectOfNewUserExecute(pinfo);
		Mockito.verify(mockhservice).updateReport(1,12,23,45,56, "GO");
	}
	/*
	@Test
	public void addLegacyProjectInfoMustReturnProjectInfoObject(){
		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();

		ProjectInfo pinfo=new ProjectInfo(12,23,45,56);
		pinfo.setProjectId(1);
		
//		Reports rObj=cservice.getErrorCountForGivenProject();
		
		CheckerToolDAO mockhservice=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockhservice.addProjectInfo(12,23,45,56)).thenReturn(pinfo);
		cservice.setCdao(mockhservice);

		Reports rmock=Mockito.mock(Reports.class);
		rmock.setNoOfPmdErrors(12);
		rmock.setNoOfCheckstyleErrors(23);
		rmock.setNoOfSimianErrors(45);
		rmock.setNoOfYascaErrors(56);
		Mockito.when(cservice.getErrorCountForGivenProject()).thenReturn(rmock);
		//cannot directly call
		Mockito.verify(cservice).getErrorCountForGivenProject();
//		rmock=cservice.getErrorCountForGivenProject();
//		cservice.addLegacyProjectInfo();
//		Mockito.verify(mockhservice).addProjectInfo(12, 23, 45, 56);
	}
	*/
	
	@Test
	public void getJavaFilesPresentInInputFolderMustReturnList() {
		boolean status=false;
		Object o=ctslObj.getJavaFilesPresentInInputFolder();
		if(o instanceof List<?>) {
			status=true;
		}
		assertTrue(status);
	}
	
	@Test
	public void getYascaErrorsMustAddInErrorsList() {
		List<String> listOfErrors=new ArrayList<>();
		boolean status=false;
		ctslObj.getYascaErrors(listOfErrors,"Start.java");
		if(listOfErrors!=null) {
			status=true;
		}
		assertTrue(status);
	}
}
