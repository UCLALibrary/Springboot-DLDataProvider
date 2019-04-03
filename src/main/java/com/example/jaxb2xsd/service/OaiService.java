package com.example.jaxb2xsd.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openarchives.oai._2.DeletedRecordType;
import org.openarchives.oai._2.DescriptionType;
import org.openarchives.oai._2.GetRecordType;
import org.openarchives.oai._2.GranularityType;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.ListMetadataFormatsType;
import org.openarchives.oai._2.ListSetsType;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.MetadataType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ObjectFactory;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.RequestType;
import org.openarchives.oai._2.SetType;
import org.openarchives.oai._2.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jaxb2xsd.dao.OaiPmhDAO;
import com.example.jaxb2xsd.model.Item;
import com.example.jaxb2xsd.model.Project;

@Service
public class OaiService extends JaxbCommonMessageFactory {

	private static Log logger = LogFactory.getLog(OaiService.class);
	protected static JAXBContext jaxbContext = null;
	private static ObjectFactory ofactory = null;

	private String identifier = null;
	private String set = null;
	private String metadataPrefix = null;
	private String resumptionToken = null;

	private String from = null;
	private String until = null;
	private boolean generateResumptionToken = false;
	private int itemCount = 0;

	private int startRow = 0;
	private int endRow = 0;

	@Autowired
	private OaiPmhDAO oaiPmhDao;

	@Autowired
	private OaiIdentifierService oaiIdentifierService;

	@Autowired
	private OaiFriendsService oaiFriendsService;

	@Autowired
	private OaiDCService oaiDCService;
	
	@Autowired
	private OaiMODService oaiMODsService;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public boolean isGenerateResumptionToken() {
		return generateResumptionToken;
	}

	public void setGenerateResumptionToken(boolean generateResumptionToken) {
		this.generateResumptionToken = generateResumptionToken;
	}

	public OaiService() throws JAXBException {
		if (jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance("org.openarchives.oai._2");
		}

		if (ofactory == null) {
			ofactory = new ObjectFactory();
		}

	}

	public String identify(String servletPath) throws Exception {
		DescriptionType description = null;

		OAIPMHtype oaipmh = ofactory.createOAIPMHtype();
		oaipmh.setResponseDate(getResponseDate());
		RequestType requestType = ofactory.createRequestType();
		requestType.setVerb(VerbType.IDENTIFY);
		requestType.setValue(servletPath);
		oaipmh.setRequest(requestType);
		IdentifyType identify = ofactory.createIdentifyType();
		identify.setRepositoryName("Digital Library Collection System");
		identify.setBaseURL(servletPath);
		identify.setProtocolVersion("2.0");
		identify.getAdminEmail().add("pghorpade@library.ucla.edu");
		identify.getAdminEmail().add("sdavison@library.ucla.edu");
		identify.getAdminEmail().add("hchiong@library.ucla.edu");
		identify.setEarliestDatestamp("2002-11-07");
		identify.setDeletedRecord(DeletedRecordType.NO);
		identify.setGranularity(GranularityType.YYYY_MM_DD);
		description = ofactory.createDescriptionType();
		description.setAny(oaiIdentifierService.buildXml());
		identify.getDescription().add(description);
		description = ofactory.createDescriptionType();
		description.setAny(oaiFriendsService.buildXml());
		identify.getDescription().add(description);
		oaipmh.setIdentify(identify);
		String temp = toXML(ofactory.createOAIPMH(oaipmh), jaxbContext,
				"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd")
						.replaceAll("xmlns:oai-identifier=\"http://www.openarchives.org/OAI/2.0/oai-identifier\"", "")
						.replaceAll("xmlns:friends=\"http://www.openarchives.org/OAI/2.0/friends/\"", "")
						.replaceAll(":friends", "").replaceAll(":oai-identifier", "");
		return temp;
	}

	public String listMetaDataFormats(String servletPath) throws Exception {
		String temp = null;
		MetadataFormatType metatdata = null;
		OAIPMHtype oaipmh = ofactory.createOAIPMHtype();
		oaipmh.setResponseDate(getResponseDate());
		RequestType requestType = ofactory.createRequestType();
		requestType.setVerb(VerbType.LIST_METADATA_FORMATS);
		requestType.setValue(servletPath);
		if (null != identifier) {
			requestType.setIdentifier(identifier);
		}
		oaipmh.setRequest(requestType);
		ListMetadataFormatsType format = ofactory.createListMetadataFormatsType();
		metatdata = ofactory.createMetadataFormatType();
		metatdata.setMetadataNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
		metatdata.setMetadataPrefix("oai_dc");
		metatdata.setSchema("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
		format.getMetadataFormat().add(metatdata);
		metatdata = ofactory.createMetadataFormatType();
		metatdata.setMetadataNamespace("http://www.loc.gov/mods/v3");
		metatdata.setMetadataPrefix("mods");
		metatdata.setSchema("http://www.loc.gov/standards/mods/v3/mods-3-3.xsd");
		format.getMetadataFormat().add(metatdata);
		oaipmh.setListMetadataFormats(format);
		temp = toXML(ofactory.createOAIPMH(oaipmh), jaxbContext,
				"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
		return temp;
	}

	public String listSets(String servletPath) throws Exception {

		String temp = null;
		OAIPMHtype oaipmh = ofactory.createOAIPMHtype();
		oaipmh.setResponseDate(getResponseDate());
		RequestType requestType = ofactory.createRequestType();
		requestType.setVerb(VerbType.LIST_SETS);
		requestType.setValue(servletPath);

		oaipmh.setRequest(requestType);
		ListSetsType listSets = ofactory.createListSetsType();
		List<SetType> sets = listSets.getSet();
		List<Project> projects = oaiPmhDao.getOAISets();
		if (null != projects && !projects.isEmpty()) {
			for (Project project : projects) {
				// if("yes".equals(project.getOaiFlag())){ //TODO REMOVE THIS COMMENT WHEN NEW
				// COLUMN ADDED TO DLCS PRODUCTION DATABASE SERVER

				SetType set = ofactory.createSetType();
				set.setSetSpec(project.getWebappName());
				set.setSetName(project.getTitle());
				sets.add(set);
				// }
			}

		}
		oaipmh.setListSets(listSets);
		temp = toXML(ofactory.createOAIPMH(oaipmh), jaxbContext,
				"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
		return temp;
	}

	public String getRecord(String servletPath) throws Exception {
		String temp = null;
		String itemArk = null;
		if (null != identifier) {

			itemArk = identifier.substring(identifier.indexOf("/") + 1, identifier.length()).replace('-', '/');
			List<Item> items = oaiPmhDao.getRecordForOai(itemArk);
			if (null != items && !items.isEmpty()) {
				Item item = items.get(0);

				OAIPMHtype oaipmh = ofactory.createOAIPMHtype();
				oaipmh.setResponseDate(getResponseDate());
				RequestType requestType = ofactory.createRequestType();
				requestType.setVerb(VerbType.GET_RECORD);
				requestType.setValue(servletPath);
				requestType.setIdentifier(identifier);
				requestType.setMetadataPrefix(metadataPrefix);
				oaipmh.setRequest(requestType);
				GetRecordType getRecord = ofactory.createGetRecordType();
				RecordType record = ofactory.createRecordType();
				HeaderType header = ofactory.createHeaderType();
				header.setIdentifier(identifier);
				header.setDatestamp(demonstrateSimpleDateFormatFormatting());
				record.setHeader(header);
				MetadataType metadata = ofactory.createMetadataType();
				if ("oai_dc".equals(metadataPrefix)) {
					metadata.setAny(oaiDCService.buildOAIDCXML(item));
				} else {
					metadata.setAny(toDom(oaiMODsService.buildModsXML(item), JAXBContext.newInstance("gov.loc.mods"),
							"http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-3.xsd"));
				}

				record.setMetadata(metadata);
				getRecord.setRecord(record);
				oaipmh.setGetRecord(getRecord);
				temp = toXML(ofactory.createOAIPMH(oaipmh), jaxbContext,
						"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");

				return temp.replaceAll("xmlns=\"\"", "")
						.replaceAll("xmlns:ns5=\"http://www.openarchives.org/OAI/2.0/\"", "");
			}
		}
		return null;
	}

}
