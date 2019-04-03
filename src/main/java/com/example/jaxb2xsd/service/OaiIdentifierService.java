package com.example.jaxb2xsd.service;

import org.openarchives.oai._2_0.oai_identifier.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


import org.openarchives.oai._2_0.oai_identifier.OaiIdentifierType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class OaiIdentifierService extends JaxbCommonMessageFactory {
	protected static JAXBContext jaxbContext = null;
    private static ObjectFactory ofactory = null;

    public OaiIdentifierService() throws JAXBException {
        if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance("org.openarchives.oai._2_0.oai_identifier");
        }

        if (ofactory == null) {
                ofactory = new ObjectFactory();
        }

    }
	
	public Element buildXml() throws Exception {
		//ObjectFactory ofactory = new ObjectFactory();
        OaiIdentifierType identifier = ofactory.createOaiIdentifierType();
        identifier.setScheme("oai");
        identifier.setRepositoryIdentifier("library.ucla.edu");
        identifier.setDelimiter(":");
        identifier.setSampleIdentifier("oai:library.ucla.edu:digital2/21198-zz0002ksb2");
       // return identifier;
        return toDom(ofactory.createOaiIdentifier(identifier),jaxbContext,"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd");
    }
	
}	
	