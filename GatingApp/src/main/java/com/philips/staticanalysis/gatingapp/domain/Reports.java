package com.philips.staticanalysis.gatingapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Reports {
	@Id
	@Column(name="reportid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int reportId;
	
	@Column(name="projectid")
	int projectId;
	
	@Column(name="noOfPmdErrors")
	int noOfPmdErrors;
	
	@Column(name="noOfCheckstyleErrors")
	int noOfCheckstyleErrors;
	
	@Column(name="noOfSimianErrors")
	int noOfSimianErrors;
	
	@Column(name="noOfYascaErrors")
	int noOfYascaErrors;
	
	@Column(name = "resultOfRun")
	String resultOfRun;
	
	@ManyToOne()
	@JoinColumn(name = "projectId", insertable = false, updatable = false)
	ProjectInfo pinfo;
	
	public ProjectInfo getPinfo() {
		return pinfo;
	}

	public void setPinfo(ProjectInfo pinfo) {
		this.pinfo = pinfo;
	}

	public Reports() {
	}

	public Reports(int projectId, int noOfPmdErrors, int noOfCheckstyleErrors, int noOfSimianErrors,
			int noOfYascaErrors, String resultOfRun) {
		super();
		this.projectId = projectId;
		this.noOfPmdErrors = noOfPmdErrors;
		this.noOfCheckstyleErrors = noOfCheckstyleErrors;
		this.noOfSimianErrors = noOfSimianErrors;
		this.noOfYascaErrors = noOfYascaErrors;
		this.resultOfRun = resultOfRun;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getNoOfPmdErrors() {
		return noOfPmdErrors;
	}

	public void setNoOfPmdErrors(int noOfPmdErrors) {
		this.noOfPmdErrors = noOfPmdErrors;
	}

	public int getNoOfCheckstyleErrors() {
		return noOfCheckstyleErrors;
	}

	public void setNoOfCheckstyleErrors(int noOfCheckstyleErrors) {
		this.noOfCheckstyleErrors = noOfCheckstyleErrors;
	}

	public int getNoOfSimianErrors() {
		return noOfSimianErrors;
	}

	public void setNoOfSimianErrors(int noOfSimianErrors) {
		this.noOfSimianErrors = noOfSimianErrors;
	}

	public int getNoOfYascaErrors() {
		return noOfYascaErrors;
	}

	public void setNoOfYascaErrors(int noOfYascaErrors) {
		this.noOfYascaErrors = noOfYascaErrors;
	}

	public String getResultOfRun() {
		return resultOfRun;
	}

	public void setResultOfRun(String resultOfRun) {
		this.resultOfRun = resultOfRun;
	}

	@Override
	public String toString() {
		return "Reports [reportId=" + reportId + ", projectId=" + projectId + ", noOfPmdErrors=" + noOfPmdErrors
				+ ", noOfCheckstyleErrors=" + noOfCheckstyleErrors + ", noOfSimianErrors=" + noOfSimianErrors
				+ ", noOfYascaErrors=" + noOfYascaErrors + ", resultOfRun=" + resultOfRun + "]";
	}	
	
	
}