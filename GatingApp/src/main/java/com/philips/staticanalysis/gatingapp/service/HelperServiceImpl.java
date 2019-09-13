package com.philips.staticanalysis.gatingapp.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HelperServiceImpl implements HelperService{

	private static org.apache.log4j.Logger log = Logger.getLogger(CheckerToolServiceImpl.class);

	@Override
	public List<String> readAFile(String filename){
		System.out.println("In helper service");
		List<String> listOfFile=new ArrayList<>();
		boolean status=exists(filename);
		if(status) {
			try (BufferedReader br = new BufferedReader(new FileReader(filename))){
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					listOfFile.add(sCurrentLine);
				}
			} 
			catch (IOException e) {
				log.error(e.getMessage());		
			}	
		}
		return listOfFile;
	}
	
	public boolean exists(String name){
		Path path=Paths.get(name);
		return path.toFile().exists();
	}
}


