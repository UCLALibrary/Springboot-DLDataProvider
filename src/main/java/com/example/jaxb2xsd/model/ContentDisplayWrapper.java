package com.example.jaxb2xsd.model;

public class ContentDisplayWrapper {
	
	private String displayTitle;
	private int itemSequence;
	private String displayArk;
	private String displayLocation;
	private String displayFileName;
	private String displayTableOfContent;
	private String transcriptionFileName;
	
	
	public String getDisplayTitle() {
		return displayTitle;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	public int getItemSequence() {
		return itemSequence;
	}
	public void setItemSequence(int itemSequence) {
		this.itemSequence = itemSequence;
	}
	public String getDisplayArk() {
		return displayArk;
	}
	public void setDisplayArk(String displayArk) {
		this.displayArk = displayArk;
	}
	public String getDisplayLocation() {
		return displayLocation;
	}
	public void setDisplayLocation(String displayLocation) {
		this.displayLocation = displayLocation;
	}
	public String getDisplayFileName() {
		return displayFileName;
	}
	public void setDisplayFileName(String displayFileName) {
		this.displayFileName = displayFileName;
	}
	public String getDisplayTableOfContent() {
		return displayTableOfContent;
	}
	public void setDisplayTableOfContent(String displayTableOfContent) {
		this.displayTableOfContent = displayTableOfContent;
	}
	public String getTranscriptionFileName() {
		return transcriptionFileName;
	}
	public void setTranscriptionFileName(String transcriptionFileName) {
		this.transcriptionFileName = transcriptionFileName;
	}
	@Override
	public String toString() {
		return "ContentDisplayWrapper [displayTitle=" + displayTitle + ", itemSequence=" + itemSequence
				+ ", displayArk=" + displayArk + ", displayLocation=" + displayLocation + ", displayFileName="
				+ displayFileName + ", displayTableOfContent=" + displayTableOfContent + ", transcriptionFileName="
				+ transcriptionFileName + "]";
	}
	
	

}
