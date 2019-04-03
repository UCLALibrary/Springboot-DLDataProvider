package com.example.jaxb2xsd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.purl.dc.elements._1.ObjectFactory;
import org.purl.dc.elements._1.ElementType;
import org.springframework.stereotype.Service;

import com.example.jaxb2xsd.model.DescControlValue;
import com.example.jaxb2xsd.model.Item;

@Service
public class DCService {
	protected static JAXBContext jaxbContext = null;
    private static ObjectFactory ofactory = null;
    private String identifier = null;
    private String metadataPrefix = null;
    
	public DCService()throws JAXBException {
        
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance("org.openarchives.oai._2_0.oai_dc");
        }

        if (ofactory == null) {
            ofactory = new ObjectFactory();
        }        
    }

	public void buildDC(OaiDcType oaiDC, Item currentItem) {
		// TODO Auto-generated method stub
		setTittle(oaiDC,currentItem);
        setAltTitle(oaiDC,currentItem);
        
        ElementType element =  ofactory.createElementType();
        element.setValue("http://digital2.library.ucla.edu/viewItem.do?ark="+currentItem.getArk()); // TODO CHANGE FOR PRODUCTION VERY IMPORTANT
        
        if(currentItem.getWebAppName().equalsIgnoreCase("cdli")) {
            element.setValue("http://digital2.library.ucla.edu/cdli/viewCdliItem.do?arkId="+currentItem.getArk());   
        }
        
        oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createIdentifier(element));
        // add thumbnails
        String thumbnailPath = currentItem.getThumbnailURL(); //getThumbnailURL(currentItem.getArk());
        if(thumbnailPath != null && thumbnailPath.length() > 0){
            ElementType elementThumbPath =  ofactory.createElementType();
            elementThumbPath.setValue(thumbnailPath);
            oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createIdentifier(elementThumbPath));
        }
        
        setDescription(oaiDC,currentItem);
        setPublisher(oaiDC,currentItem);
        setDate(oaiDC,currentItem);
        
        
        setFormat(oaiDC,currentItem);
        setSource(oaiDC,currentItem);
        setLanguage(oaiDC,currentItem);
        
        setRelation(oaiDC,currentItem);
        setCoverage(oaiDC,currentItem);
        setRights(oaiDC,currentItem);
        
        setSubject(oaiDC,currentItem);
        setName(oaiDC,currentItem);
        
        if (currentItem.getWebAppName().equals("cdli")) {
            setIdentifier(oaiDC, currentItem);
        }
	}
	
	 public static  Map<String,List<String>> getdescriptiveMDMap(List<DescControlValue> arrayList) {
	        Map<String,List<String>> nodeMap = new HashMap<>();
	        try {
	            String value = null;
	            //String term = null;
	            String qualifier = null;
	           // String descValuePk = null;
	            List<String> valueHolder = new ArrayList<>();
	            List<String> qualifierHolder = new ArrayList<>();
	            
	           // Map<String, String> source = new HashMap<>();
	           

	            if (arrayList != null) {
	            	
	            	

	                Iterator<DescControlValue> iter = arrayList.iterator();
	                while (iter.hasNext()) {

	                    DescControlValue descriptiveMD = (DescControlValue)iter.next();
	                    value = descriptiveMD.getValue();
	                    //term = descriptiveMD.getTermLabel();
	                    qualifier = descriptiveMD.getQualifier();
	                    //descValuePk = descriptiveMD.getDescValuePk();                   
	                    valueHolder.add(value);
	                    qualifierHolder.add(qualifier);
	                    //source.put(descValuePk,descriptiveMD.getSource());
	                    //ids.add(descValuePk);
	                }
	                nodeMap.put("VALUE", valueHolder);
	                nodeMap.put("QUALIFIER", qualifierHolder);
	                //nodeMap.put("SOURCE", source);
	               // nodeMap.put("IDS", ids);

	            }


	        } catch (Exception e) {
	           e.printStackTrace();
	            //log.error("Error in method getdescriptiveMDMap " + e.getMessage() + "<br>");
	        }
	        return nodeMap;
	    }

	    public void setIdentifier(String identifier) {
	        this.identifier = identifier;
	    }

	    public String getIdentifier() {
	        return identifier;
	    }

	    public void setMetadataPrefix(String metadataPrefix) {
	        this.metadataPrefix = metadataPrefix;
	    }

	    public String getMetadataPrefix() {
	        return metadataPrefix;
	    }

	    
		private void setTittle(OaiDcType oaiDC, Item item) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();       
	        Map<String, List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(item.getDescMetadataList("Title"));
	        tempValueHolder = tempMap.get("VALUE");
	        tempValueHolderSize = tempValueHolder.size();
	        if (tempValueHolderSize > 0) {
	            ElementType element = ofactory.createElementType();
	            element.setValue((String)tempValueHolder.get(0));
	            oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createTitle(element));
	        }
	    }
	    
	    private void setIdentifier(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();
	        List<String> tempQualifierHolder = new ArrayList<>();
	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = getdescriptiveMDMap(currentItem.getDescMetadataList("AltIdentifier"));
	        if (tempMap != null) {
	        tempValueHolder =  tempMap.get("VALUE");
	        tempQualifierHolder = tempMap.get("QUALIFIER");
	        tempValueHolderSize = tempValueHolder.size();
	        
	            if (tempValueHolderSize > 0) {
	                for (int i = 0; i < tempValueHolderSize; i++) { 
	                    ElementType element = ofactory.createElementType();
	                    
	                
	                    if("museumNumber".equals(tempQualifierHolder.get(i))) {
	                        element.setValue("museum number: "  + (String)tempValueHolder.get(i));
	                    }
	                
	                    if("accessionNumber".equals(tempQualifierHolder.get(i))) {
	                        element.setValue("accession number: " + (String)tempValueHolder.get(i));
	                    }
	                
	                    if("cdli".equals(tempQualifierHolder.get(i))) {
	                        element.setValue("CDLI p-number: " + (String)tempValueHolder.get(i));
	                    }
	                
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createIdentifier(element));
	                }
	            }
	        }
	    }
	    
	    
	    
	    private void setAltTitle(OaiDcType oaiDC, Item item){
	        
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();       
	        Map<String, List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(item.getDescMetadataList("AltTitle"));
	        tempValueHolder = tempMap.get("VALUE");
	        tempValueHolderSize = tempValueHolder.size();
	        if (tempValueHolderSize > 0) {
	            for (int i = 0; i < tempValueHolderSize; i++) {
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i) + " [alternative title]");
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createTitle(element));
	            }
	        }
	        
	    }

	    private void setDescription(OaiDcType oaiDC, Item currentItem) {
	        
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();
	        //List<String> tempQualifierHolder = new ArrayList<>();
	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Description"));
	        tempValueHolder = tempMap.get("VALUE");
	        //tempQualifierHolder = tempMap.get("QUALIFIER");
	        tempValueHolderSize = tempValueHolder.size();

	        if (tempValueHolderSize > 0) {

	            for (int i = 0; i < tempValueHolderSize; i++) {
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createDescription(element));
	            }
	        }
	    }

	    private void setPublisher(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();       
	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Publisher"));
	        tempValueHolder = tempMap.get("VALUE");
	        tempValueHolderSize = tempValueHolder.size();
	        if (tempValueHolderSize > 0) {
	            for (int i = 0; i < tempValueHolderSize; i++) {
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createPublisher(element));
	            }
	        }
	    }

	    private void setDate(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();
	        //List<String> tempQualifierHolder = new ArrayList<>();
	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Date"));
	        tempValueHolder = tempMap.get("VALUE");
	        //tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createDate(element));
	            }
	        }
	    }

	    private void setFormat(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();
	        
	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Format"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createFormat(element));
	            }
	        }
	    }

	    private void setSource(OaiDcType oaiDC, Item currentItem) {
	        
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Source"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createSource(element));
	            }
	        }
	    }

	    private void setLanguage(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Language"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createLanguage(element));
	            }
	        }
	    }

	    private void setRelation(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Relation"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createRelation(element));
	            }
	        }
	    }

	    private void setCoverage(OaiDcType oaiDC, Item currentItem) {
	        
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Coverage"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createCoverage(element));
	            }
	        }
	    }

	    private void setRights(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Rights"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createRights(element));
	            }
	        }
	    }

	    private void setSubject(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();

	        Map<String,List<String>> tempMap = new HashMap<>();
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Subject"));
	        tempValueHolder = tempMap.get("VALUE");
	        
	        tempValueHolderSize = tempValueHolder.size();
	        
	        if (tempValueHolderSize > 0) {
	            
	            for (int i = 0; i < tempValueHolderSize; i++) { 
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createSubject(element));
	            }
	        }
	        
	    }

	    private void setName(OaiDcType oaiDC, Item currentItem) {
	        int tempValueHolderSize = 0;
	        List<String> tempValueHolder = new ArrayList<>();
	        List<String> tempQualifierHolder = new ArrayList<>();
	        Map<String,List<String>> tempMap = new HashMap<>();                
	        
	        tempMap = 
	                getdescriptiveMDMap(currentItem.getDescMetadataList("Name"));
	        tempValueHolder = tempMap.get("VALUE");
	        tempQualifierHolder = tempMap.get("QUALIFIER");
	        tempValueHolderSize = tempValueHolder.size();

	        if (tempValueHolderSize > 0) {
	            
	                            
	            for (int i = 0; i < tempValueHolderSize; i++) {
	                ElementType element = ofactory.createElementType();
	                element.setValue((String)tempValueHolder.get(i));
	                if("creator".equals(tempQualifierHolder.get(i))|| "author".equals(tempQualifierHolder.get(i)) || "composer".equals(tempQualifierHolder.get(i)) || "interviewee".equals(tempQualifierHolder.get(i))){
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createCreator(element));
	                } else if("contributor".equals(tempQualifierHolder.get(i)) || "annotator".equals(tempQualifierHolder.get(i)) || "compiler".equals(tempQualifierHolder.get(i))
	                || "editor".equals(tempQualifierHolder.get(i)) || "interviewer".equals(tempQualifierHolder.get(i)) || "translator".equals(tempQualifierHolder.get(i))){
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createContributor(element));
	                } else if("subject".equals(tempQualifierHolder.get(i))){
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createSubject(element));
	                } else if("publisher".equals(tempQualifierHolder.get(i))){
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createPublisher(element));
	                } else if ("repository".equals(tempQualifierHolder.get(i))){
	                    oaiDC.getTitleOrCreatorOrSubject().add(ofactory.createContributor(element));
	                }
	            }
	        }
	    }

}
