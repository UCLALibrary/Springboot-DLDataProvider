package com.example.jaxb2xsd.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;

import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ObjectFactory;
import org.openarchives.oai._2.RequestType;
import org.openarchives.oai._2.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class JaxbCommonMessageFactory {
	
	 /** Pattern to use for String representation of Dates/Times. */
    private final String dateTimeFormatPattern = "yyyy-MM-dd";
    
    /**
     * java.util.Date instance representing now that can
     * be formatted using SimpleDateFormat based on my
     * dateTimeFormatPattern field.
     */
    private final Date now = new Date();
	
	
	
	/*public  Jaxb2Marshaller createJaxb2Marshaller(String contextPath, String schemaResource, String schemaLocation) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		marshaller.setSchema(new ClassPathResource(schemaResource));
		
		Map<String, Object> properties = new HashMap<>();
		properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		properties.put("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());
		properties.put(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
		marshaller.setMarshallerProperties(properties);
		return marshaller;
	}*/
	
	/**
	    * Demonstrate presenting java.util.Date as String matching
	    * provided pattern via use of SimpleDateFormat.
	    */
	   public String demonstrateSimpleDateFormatFormatting()
	   {
	      final DateFormat format = new SimpleDateFormat(dateTimeFormatPattern);
	      final String nowString = format.format(now);
	      return nowString;
	   }
	
	 protected OAIPMHtype generateOAIPMH(HttpServletRequest request, ObjectFactory of, VerbType requestType) throws DatatypeConfigurationException {

	        String baseUrl = String.format("%s://%s:%d/oaipmh", request.getScheme(), request.getServerName(), request.getServerPort());
	        OAIPMHtype oaipmh = of.createOAIPMHtype();
	        GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime(new Date());
	        XMLGregorianCalendar cal2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	        oaipmh.setResponseDate(cal2);

	        RequestType rt = generateRequestType(request, of, baseUrl, requestType);

	        oaipmh.setRequest(rt);
	        return oaipmh;
	    }

	    private RequestType generateRequestType(HttpServletRequest request, ObjectFactory of, String baseUrl, VerbType requestType) {
	        RequestType rt = of.createRequestType();
	        rt.setVerb(requestType);
	        rt.setValue(baseUrl);

	        Enumeration<String> requestParamters = request.getParameterNames();
	        while (requestParamters.hasMoreElements()) {
	            String requestParam = requestParamters.nextElement();
	            switch (requestParam) {
	                case "metadataPrefix":
	                    rt.setMetadataPrefix(request.getParameter("metadataPrefix"));
	                    break;
	                case "from":
	                    rt.setFrom(request.getParameter("from"));
	                    break;
	                case "until":
	                    rt.setUntil(request.getParameter("until"));
	                    break;
	                case "set":
	                    rt.setSet(request.getParameter("set"));
	                    ;
	                    break;
	                case "resumptionToken":
	                    rt.setResumptionToken(request.getParameter("resumptionToken"));
	                    break;
	                case "identifier":
	                    rt.setIdentifier(request.getParameter("identifier"));
	                    break;
	            }
	        }
	        return rt;
	    }
	
	    
	    public Element toDom(Object obj, JAXBContext jctx, String location) throws Exception {
	 	   
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		    dbf.setNamespaceAware(true);
	            DocumentBuilder db = dbf.newDocumentBuilder(); 
	            Document doc = db.newDocument();
		    if (obj==null) {
		            throw new IllegalArgumentException("toXML(): pre-condition - obj is NULL !");
		    }
		   
		    final Marshaller m = jctx.createMarshaller();
		    
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, location);
		    m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());
		    m.marshal(obj, doc);
		    return doc.getDocumentElement();
	           
		}
	
	
	/**
	 * convert an object to XML format
	 */
	public String toXML(Object obj, JAXBContext jctx, String location) throws Exception {
		final StringWriter bout = new StringWriter();
                    //ByteArrayOutputStream bout= new ByteArrayOutputStream();
                    //StringWriter bout = new StringWriter();
                    
		toOutputStream(obj, jctx, location, bout);
		return bout.toString();
	}

	/**
	 * convert an object to XML format
	 */
	public void toOutputStream (Object obj, JAXBContext jctx, String location, Writer outputStream) throws Exception {
		//CHECK: pre-condition
		if (obj==null) {
			throw new IllegalArgumentException("toXML(): pre-condition - obj is NULL !");
		}
		
		final Marshaller m = jctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, location);
                m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());
		m.marshal(obj, outputStream);
		//createJaxb2Marshaller(contextPath,schemaFilelocation, namespaceLocation).marshal(obj, new StreamResult(outputStream));
                System.out.println("output:\n"+outputStream.toString());
	}


	
        
    public XMLGregorianCalendar getResponseDate() {
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
    }

}

