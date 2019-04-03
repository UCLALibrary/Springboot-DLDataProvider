package com.example.jaxb2xsd.service;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.openarchives.oai._2_0.friends.FriendsType;
import org.openarchives.oai._2_0.friends.ObjectFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class OaiFriendsService extends JaxbCommonMessageFactory {

	protected static JAXBContext jaxbContext = null;
    private static ObjectFactory ofactory = null;
    public OaiFriendsService() throws JAXBException {
    	if (jaxbContext == null) {
    		jaxbContext = JAXBContext.newInstance("org.openarchives.oai._2_0.friends");
    	}
    	if (ofactory == null) {
    		ofactory = new ObjectFactory();
    	}
    }   
	public Element buildXml() throws Exception {
		//ObjectFactory ofactory = new ObjectFactory();
        FriendsType friends = ofactory.createFriendsType();
        friends.getBaseURL().add("http://volcano.mse.jhu.edu/cgi-bin/OAI/XMLFile/LevyTest/oai.pl");
        friends.getBaseURL().add("http://reason.dlib.indiana.edu:8090/oaicat/OAIHandler");
        return toDom(ofactory.createFriends(friends),jaxbContext,"http://www.openarchives.org/OAI/2.0/friends/ http://www.openarchives.org/OAI/2.0/friends.xsd");
    }
}
