package com.example.jaxb2xsd;

import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamResult;


import org.openarchives.oai._2.DeletedRecordType;
import org.openarchives.oai._2.DescriptionType;
import org.openarchives.oai._2.GranularityType;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ObjectFactory;
import org.openarchives.oai._2.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Jaxb2xsdApplication { //implements CommandLineRunner {
	
	//private static final Logger log = LoggerFactory.getLogger(Jaxb2xsdApplication.class);
	
	//@Autowired
	//private Jaxb2Marshaller jaxbMarshaller;
	

	public static void main(String[] args) {
		SpringApplication.run(Jaxb2xsdApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		// TODO Auto-generated method stub
//		
//		ObjectFactory ofactory = new ObjectFactory();
//		//DescriptionType description = null;
//        OAIPMHtype oaipmh = ofactory.createOAIPMHtype();
//        oaipmh.setResponseDate(getResponseDate());
//        RequestType requestType = ofactory.createRequestType();
//        requestType.setVerb(VerbType.IDENTIFY);
//        requestType.setValue("testpath");
//        oaipmh.setRequest(requestType);
//        IdentifyType identify = ofactory.createIdentifyType();
//        identify.setRepositoryName("Digital Library Collection System");
//        identify.setBaseURL("testpath");
//        identify.setProtocolVersion("2.0");
//        identify.getAdminEmail().add("pghorpade@library.ucla.edu");
//        identify.getAdminEmail().add("sdavison@library.ucla.edu");
//        identify.getAdminEmail().add("hchiong@library.ucla.edu");
//        identify.setEarliestDatestamp("2002-11-07");
//        identify.setDeletedRecord(DeletedRecordType.NO);
//        identify.setGranularity(GranularityType.YYYY_MM_DD);
//        /*description = ofactory.createDescriptionType();
//        description.setAny(oaiIdentifier.buildXml());
//        identify.getDescription().add(description);
//        description = ofactory.createDescriptionType();
//        description.setAny(oaiFriends.buildXml());
//        identify.getDescription().add(description);*/
//        oaipmh.setIdentify(identify);
//        StringWriter writer = new StringWriter();
//        jaxbMarshaller.marshal(ofactory.createOAIPMH(oaipmh), new StreamResult(writer));
//        String xml = writer.toString();
//        
//        log.info("XML: {}",xml);
//        //String temp = toXML(ofactory.createOAIPMH(oaipmh),jaxbContext,"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd").replaceAll("xmlns:oai-identifier=\"http://www.openarchives.org/OAI/2.0/oai-identifier\"","").replaceAll("xmlns:friends=\"http://www.openarchives.org/OAI/2.0/friends/\"","").replaceAll(":friends","").replaceAll(":oai-identifier","");
//        //return temp;
//    }

	/*private XMLGregorianCalendar getResponseDate() {
		// TODO Auto-generated method stub
		
		
		        try {       
		            GregorianCalendar c = new GregorianCalendar(); 
		            c.setTime(new Date());
		            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		            date2.setMillisecond(0);
		            date2.setFractionalSecond(null);
		            
		        return date2.normalize();
		            //DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).normalize();      
		        } catch (DatatypeConfigurationException e) {        
		        throw new Error(e); }   
		  

		
	}*/
		
}

