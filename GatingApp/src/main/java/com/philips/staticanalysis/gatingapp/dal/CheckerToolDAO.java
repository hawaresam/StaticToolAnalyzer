package com.philips.staticanalysis.gatingapp.dal;

import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;

public interface CheckerToolDAO {

	ProjectInfo getProjectThreshold(int projectId);

	int updateReport(int projectId, int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors, String resultOfRun);

	ProjectInfo addProjectInfo(int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors, int noOfYascaErrors);

	int updateProjectInfo(int projectId, int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors);
}
