package com.example.jaxb2xsd.model;

public class Project {

	private String oaiFlag;
	private String webappName;
	private String title;
	public String getOaiFlag() {
		return oaiFlag;
	}
	public void setOaiFlag(String oaiFlag) {
		this.oaiFlag = oaiFlag;
	}
	public String getWebappName() {
		return webappName;
	}
	public void setWebappName(String webappName) {
		this.webappName = webappName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "Project [\n oaiFlag=" + oaiFlag + "\n, webappName=" + webappName + "\n, title=" + title + "\n]";
	}
	
	
	
}
