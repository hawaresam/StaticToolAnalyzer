package com.philips.staticanalysis.gatingapp.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ProjectInfo {
	
	@Id
	@Column(name="projectid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int projectId;
	
	@Column(name="noOfPmdErrorsThreshhold")
	int noOfPmdErrorsThreshhold;
	
	@Column(name="noOfCheckstyleErrorsThreshhold")
	int noOfCheckstyleErrorsThreshhold;
	
	@Column(name="noOfSimianErrorsThreshhold")
	int noOfSimianErrorsThreshhold;
	
	@Column(name="noOfYascaErrorsThreshold")
	int noOfYascaErrorsThreshold;
	
	@OneToMany(mappedBy = "pinfo")
	List<Reports> listOfReports=new ArrayList<>();
	
	public List<Reports> getListOfReports() {
		return listOfReports;
	}

	public void setListOfReports(List<Reports> listOfReports) {
		this.listOfReports = listOfReports;
	}

	public ProjectInfo() {
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getNoOfPmdErrorsThreshhold() {
		return noOfPmdErrorsThreshhold;
	}

	public void setNoOfPmdErrorsThreshhold(int noOfPmdErrorsThreshhold) {
		this.noOfPmdErrorsThreshhold = noOfPmdErrorsThreshhold;
	}

	public int getNoOfCheckstyleErrorsThreshhold() {
		return noOfCheckstyleErrorsThreshhold;
	}

	public void setNoOfCheckstyleErrorsThreshhold(int noOfCheckstyleErrorsThreshhold) {
		this.noOfCheckstyleErrorsThreshhold = noOfCheckstyleErrorsThreshhold;
	}

	public int getNoOfSimianErrorsThreshhold() {
		return noOfSimianErrorsThreshhold;
	}

	public void setNoOfSimianErrorsThreshhold(int noOfSimianErrorsThreshhold) {
		this.noOfSimianErrorsThreshhold = noOfSimianErrorsThreshhold;
	}

	public int getNoOfYascaErrorsThreshold() {
		return noOfYascaErrorsThreshold;
	}

	public void setNoOfYascaErrorsThreshold(int noOfYascaErrorsThreshold) {
		this.noOfYascaErrorsThreshold = noOfYascaErrorsThreshold;
	}

	public ProjectInfo(int noOfPmdErrorsThreshhold, int noOfCheckstyleErrorsThreshhold, int noOfSimianErrorsThreshhold,
			int noOfYascaErrorsThreshold) {
		super();
		this.noOfPmdErrorsThreshhold = noOfPmdErrorsThreshhold;
		this.noOfCheckstyleErrorsThreshhold = noOfCheckstyleErrorsThreshhold;
		this.noOfSimianErrorsThreshhold = noOfSimianErrorsThreshhold;
		this.noOfYascaErrorsThreshold = noOfYascaErrorsThreshold;
	}

	@Override
	public String toString() {
		return "ProjectInfo [projectId=" + projectId + ", noOfPmdErrorsThreshhold=" + noOfPmdErrorsThreshhold
				+ ", noOfCheckstyleErrorsThreshhold=" + noOfCheckstyleErrorsThreshhold + ", noOfSimianErrorsThreshhold="
				+ noOfSimianErrorsThreshhold + ", noOfYascaErrorsThreshold=" + noOfYascaErrorsThreshold + "]";
	}
	
	
	
}
