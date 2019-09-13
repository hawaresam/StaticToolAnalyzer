package com.philips.staticanalysis.gatingapp.dal;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.philips.staticanalysis.gatingapp.domain.ProjectInfo;
import com.philips.staticanalysis.gatingapp.service.CheckerToolServiceImpl;

public class InMemoryCheckerToolDAOTest {
	@Test
	public void getProjectThresholdMustReturnProjectInfo() {
//		CheckerToolServiceImpl cservice=new CheckerToolServiceImpl();
		ProjectInfo pinfo=new ProjectInfo(1,2,3,4);
		pinfo.setProjectId(12);
		Object obj;
		CheckerToolDAO mockDao=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockDao.getProjectThreshold(12)).thenReturn(pinfo);
		obj=mockDao.getProjectThreshold(12);
//		cservice.getproje
		Mockito.verify(mockDao).getProjectThreshold(12);
		
		boolean status=false;
		if(obj instanceof ProjectInfo)
			status=true;
		
		assertTrue(status);
	}
	
	@Test
	public void updateReportMustReturnInteger() throws InterruptedException {
		ProjectInfo pinfo=new ProjectInfo(1,2,3,4);
		pinfo.setProjectId(12);
		CheckerToolDAO mockDao=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockDao.updateReport(1, 2, 3, 4, 5, "GO")).thenReturn(1);
		Object x=mockDao.updateReport(1, 2, 3, 4, 5, "GO");
		Mockito.verify(mockDao).updateReport(1, 2, 3, 4, 5, "GO");
		
		boolean status=false;
		if(x instanceof Integer)
			status=true;
		
		assertTrue(status);
	}
	
	@Test
	public void addProjectInfoMustReturnProjectInfo() {
		ProjectInfo pinfo=new ProjectInfo(1,2,3,4);
		pinfo.setProjectId(12);
		Object info;
		CheckerToolDAO mockDao=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockDao.addProjectInfo(1, 2, 3, 4)).thenReturn(pinfo);
		info=mockDao.addProjectInfo(1, 2, 3, 4);
		Mockito.verify(mockDao).addProjectInfo(1, 2, 3, 4);
		
		boolean status=false;
		if(info instanceof ProjectInfo)
			status=true;
		
		assertTrue(status);
	}
	
	@Test
	public void updateProjectInfoMustReturnNull() {
		Object info;
		CheckerToolDAO mockDao=Mockito.mock(CheckerToolDAO.class);
		Mockito.when(mockDao.updateProjectInfo(1, 2, 3, 4,5 )).thenReturn(1);
		info=mockDao.updateProjectInfo(1, 2, 3, 4,5);
		Mockito.verify(mockDao).updateProjectInfo(1, 2, 3, 4,5);
		
		boolean status=false;
		if(info instanceof Integer && info.equals(1))
			status=true;
		
		assertTrue(status);
	}
}
