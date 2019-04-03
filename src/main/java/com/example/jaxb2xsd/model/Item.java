package com.example.jaxb2xsd.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Item {
	
	private String ark;
	private String webAppName;
	private String series_title;
	private String series_ark;
	private String series_abstract;
	private String thumbnailURL;
	private Date lastEditDate;
	private List<ContentDisplayWrapper> sessionContent;
	private List<DescControlValue> DescValues;
	
	
	
	public String getArk() {
		return ark;
	}
	public void setArk(String ark) {
		this.ark = ark;
	}
	public String getWebAppName() {
		return webAppName;
	}
	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}
	public String getSeries_title() {
		return series_title;
	}
	public void setSeries_title(String series_title) {
		this.series_title = series_title;
	}
	public String getSeries_ark() {
		return series_ark;
	}
	public void setSeries_ark(String series_ark) {
		this.series_ark = series_ark;
	}
	public String getSeries_abstract() {
		return series_abstract;
	}
	public void setSeries_abstract(String series_abstract) {
		this.series_abstract = series_abstract;
	}
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
	public Date getLastEditDate() {
		return lastEditDate;
	}
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}
	public List<ContentDisplayWrapper> getSessionContent() {
		return sessionContent;
	}
	public void setSessionContent(List<ContentDisplayWrapper> sessionContent) {
		this.sessionContent = sessionContent;
	}
	public List<DescControlValue> getDescValues() {
		return DescValues;
	}
	public void setDescValues(List<DescControlValue> descValues) {
		DescValues = descValues;
	}
	@Override
	public String toString() {
		return "Item [ark=" + ark + ", webAppName=" + webAppName + ", series_title=" + series_title + ", series_ark="
				+ series_ark + ", series_abstract=" + series_abstract + ", thumbnailURL=" + thumbnailURL
				+ ", lastEditDate=" + lastEditDate + ", sessionContent=" + sessionContent + ", DescValues=" + DescValues
				+ "]";
	}
	
	 /**
     * Retrives all the descriptive values for the specified parameter.
     * @param coreDescTerm
     * @return
     */
    public List<DescControlValue> getDescMetadataList(String coreDescTerm) {
       List<DescControlValue> metadataList = new ArrayList<>();
        List<DescControlValue> descValuesList = getDescValues();

        if (descValuesList.size() > 0) {
            Iterator<DescControlValue> descIter = descValuesList.iterator();
            DescControlValue descValue = null;
            

            while (descIter.hasNext()) {
                descValue = (DescControlValue)descIter.next();
                if (descValue.getTermLabel().equals(coreDescTerm)) {                   
                   
                        metadataList.add(descValue);
                   
                   
                }
            }
        }
       
        
      
        return metadataList;
    }

}
