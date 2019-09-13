package com.philips.staticanalysis.gatingapp.dal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;
import com.philips.staticanalysis.gatingapp.domain.Reports;

@Transactional
@Repository
public class InMemoryCheckerToolDAO implements CheckerToolDAO{
	@PersistenceContext
	EntityManager em;

	@Override
	public ProjectInfo getProjectThreshold(int projectId) {
		return em.find(ProjectInfo.class, projectId);
	}

	@Override
	public int updateReport(int projectId, int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors, String resultOfRun) {
		Reports rObj=new Reports();
		rObj.setProjectId(projectId);
		rObj.setNoOfPmdErrors(noOfPmdErrors);
		rObj.setNoOfCheckstyleErrors(noOfCheckstyleErrors);
		rObj.setNoOfSimianErrors(noOfSimianErrors);
		rObj.setNoOfYascaErrors(noOfYascaErrors);
		rObj.setResultOfRun(resultOfRun);
		
		em.persist(rObj);
		
		return rObj.getProjectId();
	}

	@Override
	public ProjectInfo addProjectInfo(int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors) {
		ProjectInfo p=new ProjectInfo();
		p.setNoOfPmdErrorsThreshhold(noOfPmdErrors);
		p.setNoOfCheckstyleErrorsThreshhold(noOfCheckstyleErrors);
		p.setNoOfSimianErrorsThreshhold(noOfSimianErrors);
		p.setNoOfYascaErrorsThreshold(noOfYascaErrors);
		
		em.persist(p);
		
		return p;	
	}

	@Override
	public int updateProjectInfo(int projectId, int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors) {
		em.createQuery("update ProjectInfo set noOfPmdErrorsThreshhold=:firstParam, noOfCheckstyleErrorsThreshhold=:secondParam,"
				+ "noOfSimianErrorsThreshhold=:thirdParam, noOfYascaErrorsThreshold=:fouthParam where projectId=:fifthParam").setParameter("firstParam", noOfPmdErrors).
				setParameter("secondParam", noOfCheckstyleErrors).setParameter("thirdParam", noOfSimianErrors).setParameter("fouthParam", noOfYascaErrors).
				setParameter("fifthParam", projectId).executeUpdate();
		return projectId;
	}

}
