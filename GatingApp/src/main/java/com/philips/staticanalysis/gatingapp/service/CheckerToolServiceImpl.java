package com.philips.staticanalysis.gatingapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.staticanalysis.gatingapp.dal.CheckerToolDAO;
import com.philips.staticanalysis.gatingapp.domain.CheckerTool;
import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;
import com.philips.staticanalysis.gatingapp.domain.Reports;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class CheckerToolServiceImpl implements CheckerToolService{

	CheckerToolDAO cdao;
	private static org.apache.log4j.Logger log = Logger.getLogger(CheckerToolServiceImpl.class);
	
	static String cloneDirectoryPath;
	static String configFilePath;
	static List<CheckerTool> listOfTools=new ArrayList<>();
	static final String OUTPUT="output";
	static final String YASCA="Yasca";
	
	static
	{
		readThePathConfigFile();
		
		CheckerTool ct=new CheckerTool();
		try (Scanner scan = new Scanner(new File(configFilePath))){
	        while(scan.hasNextLine()) {
	            String[] split = scan.nextLine().trim().split("=");
	            if(split.length == 2 && split[0].equals("toolName")) {
	            	ct.setToolName(split[1]);
	            }
	            else if(split.length == 2 && split[0].equals("toolExeFile")) {
	            	ct.setToolExeFilePath(split[1]);
	            }
	            else if(split.length == 2 && split[0].equals("toolCommand")) {
	            	ct.setToolCommand(split[1]);
	            }
	            ct=addToToolsList(ct);
	        }	        
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
		
		for(CheckerTool t:listOfTools) {
			String cmd=t.getToolCommand();
			cmd=cmd.replace("toolExeFile", t.getToolExeFilePath());
			cmd=cmd.replace("toolInputFile", cloneDirectoryPath);
			if(t.getToolName().equals(YASCA)) 
				cmd=cmd.replace("toolOutputFile", "C:\\Users\\320065418\\Music\\GatingApp\\GatingApp\\output"+t.getToolName()+".csv");
			else
				cmd=cmd.replace("toolOutputFile", OUTPUT+""+t.getToolName()+".txt");
						
			t.setToolCommand(cmd);
		}
	} 	 

//	public CheckerToolServiceImpl() {
//		
//	}
	
	 static void readThePathConfigFile() {
		final ResourceBundle props=ResourceBundle.getBundle("path");
		cloneDirectoryPath = props.getString("InputFilePath");
		configFilePath=props.getString("ConfigurationFilePath");	
	}

	private static CheckerTool addToToolsList(CheckerTool ct) {
        if(ct.getToolName()!=null && ct.getToolCommand()!=null && ct.getToolExeFilePath()!=null) {
        	listOfTools.add(ct);
            System.out.println("Adding tool: "+ct.getToolName());
        	ct=new CheckerTool();
        }		
        return ct;
	}

	@Autowired
	public void setCdao(CheckerToolDAO cdao) {
		this.cdao = cdao;
	}

	@Override
	public void cloneRepo(String pathOfGitRepo) {
		
		File directory=new File(cloneDirectoryPath);
		try {
			FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		try {
		    Git clone=Git.cloneRepository()
		        .setURI(pathOfGitRepo)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .call();
		    clone.close();
		} catch (GitAPIException e) {
		    log.error("ERROR IN CLONING REPO FROM GIT"+e.getMessage());
		}		
	}

	@Override
	public int addNewProjectInfo() {
		return cdao.addProjectInfo(0,0,0,0).getProjectId();
	}

	@Override
	public void runTools() throws InterruptedException {
		for(CheckerTool t:listOfTools) {
			Process processObj;
			try {
				processObj = Runtime.getRuntime().exec(t.getToolCommand());
	            log.info("Waiting for process of the tool: "+t.getToolName());
				processObj.waitFor();
			} catch (IOException e1) {
				log.error(e1.getMessage());
			}
		}
	}

	@Override
	public String checkIfProjectIsGo(int projectId) {
		Reports rObj=getErrorCountForGivenProject();
			
		String resultOfRun="NO GO";
		ProjectInfo pinfo=cdao.getProjectThreshold(projectId);
		
		//compare the reports
		if(pinfo==null) {
			pinfo=cdao.addProjectInfo(0, 0, 0, 0);
		}
		
		if(rObj.getNoOfPmdErrors()<=pinfo.getNoOfPmdErrorsThreshhold() 
				&& rObj.getNoOfCheckstyleErrors()<=pinfo.getNoOfCheckstyleErrorsThreshhold()
				&& rObj.getNoOfSimianErrors()<=pinfo.getNoOfSimianErrorsThreshhold()
				&& rObj.getNoOfYascaErrors()<=pinfo.getNoOfYascaErrorsThreshold()) {
			resultOfRun="GO";
			
			cdao.updateProjectInfo(projectId,rObj.getNoOfPmdErrors(),rObj.getNoOfCheckstyleErrors(),rObj.getNoOfSimianErrors(),rObj.getNoOfYascaErrors());
		}
		
		//update the reports
		cdao.updateReport(projectId,rObj.getNoOfPmdErrors(),rObj.getNoOfCheckstyleErrors(),rObj.getNoOfSimianErrors(),rObj.getNoOfYascaErrors(),resultOfRun);
		//give final result
		return resultOfRun;
	}

	private Reports getErrorCountForGivenProject() {
		Reports rObj=new Reports();
		rObj.setNoOfPmdErrors(countPmdError());
		rObj.setNoOfCheckstyleErrors(countCheckstyleError());
		rObj.setNoOfSimianErrors(countSimianError());
		rObj.setNoOfYascaErrors(countYascaError());
		if(rObj.getNoOfCheckstyleErrors()==-1 || rObj.getNoOfSimianErrors()==-1)
			log.error("Error in getting error count from Checkstyle or Simian");
		return rObj;
	}

	private int countYascaError() {
        int count=0;
        try (CSVReader reader = new CSVReader(new FileReader("./outputYasca.csv"))){
            while ((reader.readNext()) != null)
            	count++;
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
        return count-1;
	}

	private int countSimianError() {
		try (BufferedReader br = new BufferedReader(new FileReader("./outputSimian.txt"))){
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
			    if(sCurrentLine.contains("Found") && sCurrentLine.contains("duplicate lines in") && sCurrentLine.contains("blocks in") && sCurrentLine.contains("files")) {
			    	String[] arrayOfWords=sCurrentLine.split(" ");
			    	return Integer.parseInt(arrayOfWords[1]);
			    }
			}
		} 
		catch (IOException e) {
			log.error(e.getMessage());		
		}
		return -1;
	}

	private int countCheckstyleError() {
		String path = "./outputCheckstyle.txt";
	    String lastLine = "";
		String sCurrentLine;
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			while ((sCurrentLine = br.readLine()) != null) {
			    lastLine = sCurrentLine;
			}
		} 
		catch (IOException e) {
			log.error(e.getMessage());		
		}
		int noOfErrors= countNoOfLinesInAFile(path)-3;
		if(lastLine.equals("Checkstyle ends with "+noOfErrors+" errors.")) {
			return noOfErrors;
		}
		else if(noOfErrors==-1) {
			return 0;
		}
		return -1;
	}

	private int countPmdError() {
		String path = "./outputPMD.txt";
		return countNoOfLinesInAFile(path);
	}

	private int countNoOfLinesInAFile(String path) {
		int lines = 0;
		@SuppressWarnings("unused")
		String line=null;
		try (BufferedReader reader=new BufferedReader(new FileReader(path))){
			while ((line=reader.readLine()) != null) lines++;
		} catch (IOException e) {
			log.error(e.getMessage());
		} 
		return lines;
	}

	@Override
	public ProjectInfo addLegacyProjectInfo() {
		Reports rObj=getErrorCountForGivenProject();
		return cdao.addProjectInfo(rObj.getNoOfPmdErrors(),rObj.getNoOfCheckstyleErrors(),rObj.getNoOfSimianErrors(),rObj.getNoOfYascaErrors());
	}

	@Override
	public String newProjectOfNewUserExecute(String pathOfGitRepo) throws InterruptedException {
		cloneRepo(pathOfGitRepo);
		int projectId=addNewProjectInfo();
		runTools();
		return checkIfProjectIsGo(projectId);
	}

	@Override
	public String legacyProjectOfNewUserExecute(String pathOfGitRepo) throws InterruptedException {
		cloneRepo(pathOfGitRepo);
		runTools();
		ProjectInfo obtainedProjectObject=addLegacyProjectInfo();
		cdao.updateReport(obtainedProjectObject.getProjectId(), obtainedProjectObject.getNoOfPmdErrorsThreshhold(),
				obtainedProjectObject.getNoOfCheckstyleErrorsThreshhold(), obtainedProjectObject.getNoOfSimianErrorsThreshhold(), 
				obtainedProjectObject.getNoOfYascaErrorsThreshold(), "GO");
		return "GO";
	}

	@Override
	public String projectOfExistingUserExecute(String pathOfGitRepo, int pid) throws InterruptedException {
		cloneRepo(pathOfGitRepo);
		runTools();
		return checkIfProjectIsGo(pid);
	}

	@Override
	public List<String> getJavaFilesPresentInInputFolder() {
		List<String> listOfJavaFiles=new ArrayList<>();
        File maindir = new File(cloneDirectoryPath);    
        if(maindir.exists() && maindir.isDirectory()) 
        { 
            File[] arr= maindir.listFiles(); 
            recursivePrint(arr,0,0,listOfJavaFiles);  
        }  
		return listOfJavaFiles;
	}

	private void recursivePrint(File[] arr, int index, int level, List<String> listOfJavaFiles) {
        if(index == arr.length) 
            return; 
        if(arr[index].isFile()) {
            if(FilenameUtils.getExtension(arr[index].getAbsolutePath()).equals("java"))
            	listOfJavaFiles.add(arr[index].getAbsolutePath());
        }      
        else if(arr[index].isDirectory()) 
            recursivePrint(arr[index].listFiles(), 0, level + 1, listOfJavaFiles); 
           
        recursivePrint(arr,++index, level, listOfJavaFiles); 
	}

	@Override
	public List<String> getErrorsFromAllTools(String filename) {
		List<String> listOfErrors=new ArrayList<>();
		for(CheckerTool t:listOfTools) {
			if(t.getToolName().equals("PMD") || t.getToolName().equals("Checkstyle")) {
				getPmdCheckstyleErrors(t.getToolName(),listOfErrors,filename);	
			}
			else if(t.getToolName().equals(YASCA)) {
				getYascaErrors(listOfErrors,filename);	
			}
		}
		return listOfErrors;
	}

	private void getYascaErrors(List<String> listOfErrors, String filename) {
		listOfErrors.add(YASCA);
        try (CSVReader reader = new CSVReader(new FileReader("./outputYasca.csv"))){
            String[] line;
            int flag=0;
            while ((line = reader.readNext()) != null) {
                if(line[5].equals("Full Location")) {
                	flag=1;
                }
                if(flag==1 && line[5].equals(filename)) {
                	listOfErrors.add(line[6]);
                }
            }
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
	}

	private void getPmdCheckstyleErrors(String toolname,List<String> listOfErrors,String filename) {
		listOfErrors.add(toolname);
		try (BufferedReader reader=new BufferedReader(new FileReader(OUTPUT+""+toolname+".txt"))){
			String line;
			while ((line = reader.readLine()) != null) {
				if(line.contains(filename)) {
					int index=line.indexOf("java:");
					listOfErrors.add(line.substring(index+5));
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} 
	}
}
