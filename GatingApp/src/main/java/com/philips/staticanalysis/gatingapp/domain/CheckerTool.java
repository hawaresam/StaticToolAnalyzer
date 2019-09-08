package com.philips.staticanalysis.gatingapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "checkertool")
public class CheckerTool {
	@Id
	@Column(name = "toolId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int toolId;
	@Column(name = "toolName")
	String toolName;
	@Column(name = "toolExeFilePath")
	String toolExeFilePath;
	@Column(name = "toolCommand")
	String toolCommand;
	
	public CheckerTool() {
	}

	public CheckerTool(String toolName, String toolExeFilePath, String toolCommand) {
		super();
		this.toolName = toolName;
		this.toolExeFilePath = toolExeFilePath;
		this.toolCommand = toolCommand;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolExeFilePath() {
		return toolExeFilePath;
	}

	public void setToolExeFilePath(String toolExeFilePath) {
		this.toolExeFilePath = toolExeFilePath;
	}

	public String getToolCommand() {
		return toolCommand;
	}

	public void setToolCommand(String toolCommand) {
		this.toolCommand = toolCommand;
	}

	@Override
	public String toString() {
		return "CheckerTool [toolId=" + toolId + ", toolName=" + toolName + ", toolExeFilePath=" + toolExeFilePath
				+ ", toolCommand=" + toolCommand + "]";
	}
	
	
}
