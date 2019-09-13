package com.philips.staticanalysis.gatingapp.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HelperServiceImplTest {
	HelperServiceImpl hser;
	
	@Before
	public void initializeObj() {
		hser=new HelperServiceImpl();
	}
	
	@Test
	public void existsShouldReturnTrue() {
		boolean status=hser.exists("C:\\Users\\320065418\\Music\\GatingApp\\GatingApp\\outputSimian.txt");		
		assertTrue(status);
	}
	
	@Test
	public void readAFileShouldReturnListOfLines() {
		boolean status=false;
		Object o=hser.readAFile("C:\\Users\\320065418\\Music\\GatingApp\\GatingApp\\outputSimian.txt");
		if (o!=null && o instanceof List<?>) {
				status=true;
			}
		assertTrue(status);
	}

}
