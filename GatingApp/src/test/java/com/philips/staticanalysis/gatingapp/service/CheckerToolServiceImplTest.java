package com.philips.staticanalysis.gatingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class CheckerToolServiceImplTest {
	
	CheckerToolServiceImpl ctslObj;
	
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
		assertEquals("Checking if cloneDirectoryPath is loaded", CheckerToolServiceImpl.cloneDirectoryPath, "C:\\Users\\320065418\\Music\\InputFolderFromGitRepo");
		assertEquals("Checking if configFilePath is loaded", CheckerToolServiceImpl.configFilePath, "C:\\Users\\320065418\\Music\\GatingApp\\GatingApp\\src\\main\\resources\\static\\config.properties");
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
	public void 
	
}
