package com.example.jaxb2xsd.model;

public class DescControlValue {

	private String controlValueFlag;
	private String coreDescControlValue;
	private long coreDescControlValueId;
	private String coreDescControlValueSource;
	private long coreDescQualifierId;
	private long coreDescTermId;
	private String descQualifier;
	private String qualifierFlag;
	private String termLabel;
	private String descValue;
	private long descValueId;
	
	
	public String getDescValue() {
		return descValue;
	}
	public void setDescValue(String descValue) {
		this.descValue = descValue;
	}
	public String getControlValueFlag() {
		return controlValueFlag;
	}
	public void setControlValueFlag(String controlValueFlag) {
		this.controlValueFlag = controlValueFlag;
	}
	public String getCoreDescControlValue() {
		return coreDescControlValue;
	}
	public void setCoreDescControlValue(String coreDescControlValue) {
		this.coreDescControlValue = coreDescControlValue;
	}
	public long getCoreDescControlValueId() {
		return coreDescControlValueId;
	}
	public void setCoreDescControlValueId(long coreDescControlValueId) {
		this.coreDescControlValueId = coreDescControlValueId;
	}
	public String getCoreDescControlValueSource() {
		return coreDescControlValueSource;
	}
	public void setCoreDescControlValueSource(String coreDescControlValueSource) {
		this.coreDescControlValueSource = coreDescControlValueSource;
	}
	public long getCoreDescQualifierId() {
		return coreDescQualifierId;
	}
	public void setCoreDescQualifierId(long coreDescQualifierId) {
		this.coreDescQualifierId = coreDescQualifierId;
	}
	public long getCoreDescTermId() {
		return coreDescTermId;
	}
	public void setCoreDescTermId(long coreDescTermId) {
		this.coreDescTermId = coreDescTermId;
	}
	public String getDescQualifier() {
		return descQualifier;
	}
	public void setDescQualifier(String descQualifier) {
		this.descQualifier = descQualifier;
	}
	public String getQualifierFlag() {
		return qualifierFlag;
	}
	public void setQualifierFlag(String qualifierFlag) {
		this.qualifierFlag = qualifierFlag;
	}
	
	public String getTermLabel() {
		return termLabel;
	}
	public void setTermLabel(String termLabel) {
		this.termLabel = termLabel;
	}
	
	public long getDescValueId() {
		return descValueId;
	}
	public void setDescValueId(long descValueId) {
		this.descValueId = descValueId;
	}
	
	
	 @Override
	public String toString() {
		return "DescControlValue [controlValueFlag=" + controlValueFlag + ", coreDescControlValue="
				+ coreDescControlValue + ", coreDescControlValueId=" + coreDescControlValueId
				+ ", coreDescControlValueSource=" + coreDescControlValueSource + ", coreDescQualifierId="
				+ coreDescQualifierId + ", coreDescTermId=" + coreDescTermId + ", descQualifier=" + descQualifier
				+ ", qualifierFlag=" + qualifierFlag + ", termLabel=" + termLabel + ", descValue=" + descValue
				+ ", descValueId=" + descValueId + "]";
	}
	/**
     * getValue
     * This function returns text if the term does NOT use CV
     * If the term IS set to use CV then the value retuned is the CV FK
     * @return 
     */
    public String getValue() {
       
          if(controlValueFlag.equals("no")){
              return descValue;
          } else {
              if(coreDescControlValueId != 0 )
                 return coreDescControlValue;
          }
      return null;
       
    }
    
      /**
       * getSource
       * This function returns text if the term does NOT use CV
       * If the term IS set to use CV then the value retuned is the CV FK
       * @return 
       */
      public String getSource() {
        
            if(!controlValueFlag.equals("no")){               
                if(coreDescControlValueId != 0)
                    return coreDescControlValueSource;
            }
        
         return null;
      }
      
      
      /**
       * 
       * @return 
       */
      public String getQualifier() {
          if (qualifierFlag == null) {
              return "Not Qualified";
          }
           if(qualifierFlag.equals("yes")){
              if (coreDescQualifierId == 0){
                return "No Qualifier Specified";
              } else {
                return descQualifier;
              }
           } else {
              return"Not Qualified";
           }  
      }
      
      /**
       * getDescValuePk()
       * This function will return the PK of the DescValue
       * @return 
       */
      public String getDescValuePk() {
        
           return String.valueOf(descValueId);
        
         
      }
	
	
}
