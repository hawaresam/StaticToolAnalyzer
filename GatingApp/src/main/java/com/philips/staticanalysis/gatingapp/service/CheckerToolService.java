package com.philips.staticanalysis.gatingapp.service;

import java.util.List;

import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;

public interface CheckerToolService {
	
	void cloneRepo(String pathOfGitRepo);

	int addNewProjectInfo();

	void runTools() throws InterruptedException;

	String checkIfProjectIsGo(int projectId);

	String newProjectOfNewUserExecute(String pathOfGitRepo) throws InterruptedException;

	String legacyProjectOfNewUserExecute(String pathOfGitRepo) throws InterruptedException;

	ProjectInfo addLegacyProjectInfo();

	String projectOfExistingUserExecute(String pathOfGitRepo, int pid) throws InterruptedException;

	List<String> getJavaFilesPresentInInputFolder();

	List<String> getErrorsFromAllTools(String filename);
}
