package com.philips.staticanalysis.gatingapp.service;

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
	HelperService hservice;
	
	@Autowired
	public void setHservice(HelperService hservice) {
		this.hservice = hservice;
	}

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
	
	 static void readThePathConfigFile() {
		final ResourceBundle props=ResourceBundle.getBundle("path");
		cloneDirectoryPath = props.getString("InputFilePath");
		configFilePath=props.getString("ConfigurationFilePath");	
	}

	private static CheckerTool addToToolsList(CheckerTool ct) {
        if(ct.getToolName()!=null && ct.getToolCommand()!=null && ct.getToolExeFilePath()!=null) {
        	listOfTools.add(ct);
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

	public Reports getErrorCountForGivenProject() {
		Reports rObj=new Reports();
		rObj.setNoOfPmdErrors(countPmdError());
		rObj.setNoOfCheckstyleErrors(countCheckstyleError());
		rObj.setNoOfSimianErrors(countSimianError());
		rObj.setNoOfYascaErrors(countYascaError());
		if(rObj.getNoOfCheckstyleErrors()==-1 || rObj.getNoOfSimianErrors()==-1)
			log.error("Error in getting error count from Checkstyle or Simian");
		return rObj;
	}

	public int countYascaError() {
        int count=0;
        try (CSVReader reader = new CSVReader(new FileReader("./outputYasca.csv"))){
            while ((reader.readNext()) != null)
            	count++;
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
        return count-1;
	}

	public int countSimianError(){
		List<String> listOfFileLines;
		listOfFileLines=hservice.readAFile("./outputSimian.txt");
		for(String x:listOfFileLines) {
		    if(x.contains("Found") && x.contains("duplicate lines in") && x.contains("blocks in") && x.contains("files")) {
		    	String[] arrayOfWords=x.split(" ");
		    	return Integer.parseInt(arrayOfWords[1]);
		    }
		}
		return -1;
	}

	public int countCheckstyleError() {
		List<String> listOfFileLines;
		listOfFileLines=hservice.readAFile("./outputCheckstyle.txt");
		String lastLine=listOfFileLines.get(listOfFileLines.size()-1);
		int noOfErrors=listOfFileLines.size()-3;
		if(lastLine.equals("Checkstyle ends with "+noOfErrors+" errors.")) {
			return noOfErrors;
		}		
		else if(noOfErrors==-1) {
			return 0;
		}
		return -1;
	}

	public int countPmdError() {
		String path = "./outputPMD.txt";
		List<String> listOfSimianErrors;		
		listOfSimianErrors=hservice.readAFile(path);		
		return listOfSimianErrors.size();
	}

	@Override
	public ProjectInfo addLegacyProjectInfo() {
		Reports rObj=getErrorCountForGivenProject();
		return cdao.addProjectInfo(rObj.getNoOfPmdErrors(),rObj.getNoOfCheckstyleErrors(),rObj.getNoOfSimianErrors(),rObj.getNoOfYascaErrors());
	}

	@Override
	public String newProjectOfNewUserExecute(int projectId) throws InterruptedException {
		runTools();
		return checkIfProjectIsGo(projectId);
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

	public void getYascaErrors(List<String> listOfErrors, String filename) {
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

	public void getPmdCheckstyleErrors(String toolname,List<String> listOfErrors,String filename) {
		listOfErrors.add(toolname);
		List<String> listOfFileLines;
		listOfFileLines=hservice.readAFile(OUTPUT+""+toolname+".txt");
		for(String x:listOfFileLines) {
			if(x.contains(filename)) {
				int index=x.indexOf("java:");
				listOfErrors.add(x.substring(index+5));
			}
		}
	}

	@Override
	public Integer getProjectIdOfNewProject(String pathOfGitRepo) {
		cloneRepo(pathOfGitRepo);
		return addNewProjectInfo(); 
	}


	@Override
	public ProjectInfo getProjectIdOfLegacyProject(String pathOfGitRepo) throws InterruptedException {
		cloneRepo(pathOfGitRepo);
		runTools();
		return addLegacyProjectInfo();
	}

	@Override
	public String legacyProjectOfNewUserExecute(ProjectInfo obtainedProjectObject) throws InterruptedException {
		cdao.updateReport(obtainedProjectObject.getProjectId(), obtainedProjectObject.getNoOfPmdErrorsThreshhold(),
				obtainedProjectObject.getNoOfCheckstyleErrorsThreshhold(), obtainedProjectObject.getNoOfSimianErrorsThreshhold(), 
				obtainedProjectObject.getNoOfYascaErrorsThreshold(), "GO");
		return "GO";
	}

	@Override
	public List<String> getSimianErrorInAList() {
		List<String> listOfSimianErrors;	
		listOfSimianErrors=hservice.readAFile("outputSimian.txt");		
		return listOfSimianErrors;
	}
}
