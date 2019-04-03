package com.example.jaxb2xsd.service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.openarchives.oai._2_0.oai_dc.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import com.example.jaxb2xsd.model.Item;

@Service
public class OaiDCService extends JaxbCommonMessageFactory {
	protected static JAXBContext jaxbContext = null;
    private static ObjectFactory ofactory = null;
    
    @Autowired
    private DCService dcService;
    
    
	public OaiDCService() throws JAXBException {
        
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance("org.openarchives.oai._2_0.oai_dc");
        }

        if (ofactory == null) {
            ofactory = new ObjectFactory();
        }        
    }
	
	
	
public Element buildOAIDCXML(Item currentItem) throws Exception {
        
        OaiDcType oaiDC = ofactory.createOaiDcType();
        
        dcService.buildDC(oaiDC,currentItem);

        
        
        return toDom(ofactory.createDc(oaiDC),jaxbContext,"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    }

    
   

   
}


