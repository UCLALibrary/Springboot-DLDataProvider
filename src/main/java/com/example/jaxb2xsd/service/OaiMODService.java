package com.example.jaxb2xsd.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.example.jaxb2xsd.model.ContentDisplayWrapper;
import com.example.jaxb2xsd.model.DescControlValue;
import com.example.jaxb2xsd.model.Item;

import gov.loc.mods.v3.CodeOrText;
import gov.loc.mods.v3.IdentifierDefinition;
import gov.loc.mods.v3.LocationDefinition;
import gov.loc.mods.v3.ModsDefinition;
import gov.loc.mods.v3.ObjectFactory;
import gov.loc.mods.v3.PartDefinition;
import gov.loc.mods.v3.PhysicalDescriptionDefinition;
import gov.loc.mods.v3.RelatedItemDefinition;
import gov.loc.mods.v3.TableOfContentsDefinition;
import gov.loc.mods.v3.TitleInfoDefinition;
import gov.loc.mods.v3.UrlDefinition;

@Service
public class OaiMODService extends JaxbCommonMessageFactory {
	
	protected static JAXBContext jaxbContext = null;
	private static ObjectFactory ofactory = null;
	public OaiMODService() throws JAXBException {
        
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance("gov.loc.mods.v3");
        }

        if (ofactory == null) {
            ofactory = new ObjectFactory();
        }        
    }
	
	
public JAXBElement<ModsDefinition> buildModsXML(Item currentItem) throws Exception {
        
        ModsDefinition mods = ofactory.createModsDefinition();        
        List<Object> modsgroup = mods.getModsGroup();       
        /** Title */
        modsgroup.add(setTitle(currentItem));  
        /** Alternate Title **/
        setAltTitle(modsgroup,currentItem);
        /** AltIdentifier **/
        setAltIdentifier(modsgroup,currentItem);
        /** Description **/
        setDescription(modsgroup,currentItem);
        /** Date **/
        setDate(modsgroup,currentItem);
        /** Type **/
        setType(modsgroup,currentItem);
        /** Language **/
        setLanguage(modsgroup,currentItem);
        /** Relation **/
        setRelation(modsgroup,currentItem);
        /** Subjects **/
        setSubjects(modsgroup,currentItem);
        /** Rights **/
        setRights(modsgroup,currentItem);
        /** Name **/
        setName(modsgroup,currentItem);
        /** Source **/
        setSource(modsgroup, currentItem);        
        /** Format **/
        setFormat(modsgroup, currentItem);
        
       if(null != currentItem.getSessionContent() && !currentItem.getSessionContent().isEmpty()){
       
           setConstituents(modsgroup, currentItem);
       }
       if(null != currentItem.getSeries_ark()){
          setSeries(modsgroup, currentItem);
       }
       
        IdentifierDefinition identifier =  ofactory.createIdentifierDefinition();        
        identifier.setType("uri");
        identifier.setValue("http://digital2.library.ucla.edu/viewItem.do?ark="+currentItem.getArk());
        modsgroup.add(identifier);
        return ofactory.createMods(mods);
    }

private TitleInfoDefinition setTitle(Item currentItem){
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();       
    Map tempMap = new HashMap();
    TitleInfoDefinition titleinfoType = ofactory.createTitleInfoDefinition();
    List<Object> titles = titleinfoType.getTitleOrSubTitleOrPartNumber();
   
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Title"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempValueHolderSize = tempValueHolder.size();
    if (tempValueHolderSize > 0) {
        //titles.add(ofactory.createBaseTitleInfoTypeTitle((String)tempValueHolder.get(0)));
    	titleinfoType.setTitle((String)tempValueHolder.get(0));
    }
    return titleinfoType;
}


public static  Map getdescriptiveMDMap(List arrayList) {
    Map nodeMap = new HashMap();
    try {
        String value = null;
        String term = null;
        String qualifier = null;
        String descValuePk = null;
        List valueHolder = new ArrayList();
        List qualifierHolder = new ArrayList();
        List ids = new ArrayList();
        Map source = new HashMap();
        

        if (arrayList != null) {

            Iterator iter = arrayList.iterator();
            while (iter.hasNext()) {

                DescControlValue descriptiveMD = (DescControlValue)iter.next();
                value = descriptiveMD.getValue();
                term = descriptiveMD.getTerm();
                qualifier = descriptiveMD.getQualifier();
                descValuePk = descriptiveMD.getDescValuePk();                    
                valueHolder.add(value);
                qualifierHolder.add(qualifier);
                source.put(descValuePk,descriptiveMD.getSource());
                ids.add(descValuePk);
            }
            nodeMap.put("VALUE", valueHolder);
            nodeMap.put("QUALIFIER", qualifierHolder);
            nodeMap.put("SOURCE", source);
            nodeMap.put("IDS", ids);

        }


    } catch (Exception e) {
        log.error("Error in method getdescriptiveMDMap " + e.getMessage() + "<br>");
    }
    return nodeMap;
}

private void setDescription(List modsgroup, Item currentItem) {
    
    /** Description */
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    Map tempMap = new HashMap();
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Description"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {

        for (int i = 0; i < tempValueHolderSize; i++) {

            if (tempQualifierHolder.get(i).equals("Not Qualified") || tempQualifierHolder.get(i).equals("caption") || tempQualifierHolder.get(i).equals("note")){
                NoteType note = ofactory.createNoteType();
                note.setValue((String)tempValueHolder.get(i));
                modsgroup.add(note);
            } else if(tempQualifierHolder.get(i).equals("abstract")){
                AbstractType abs = ofactory.createAbstractType();
                abs.setValue((String)tempValueHolder.get(i));
                modsgroup.add(abs);
            } else if(tempQualifierHolder.get(i).equals("biographicalNote")){
                NoteType note = ofactory.createNoteType();
                note.setStringPlusDisplayLabelPlusTypetype("biographical");
                note.setDisplayLabel("Biographical Information");
                note.setValue((String)tempValueHolder.get(i));
                modsgroup.add(note);
                
            } else {
                NoteType note = ofactory.createNoteType();
                if(tempQualifierHolder.get(i).equals("adminnote")){
                    String type = (String)tempValueHolder.get(i);
                    String value = type.substring(type.indexOf(":")+1,type.length());
                    type = type.substring(0,type.indexOf(":"));     
                    if("SUPPORTING DOCUMENTS".equals(type)){
                        note.setStringPlusDisplayLabelPlusTypetype("additional information");
                        note.setDisplayLabel("Supporting Documents");
                    }else if("INTERVIEWER BACKGROUND AND PREPARATION".equals(type)){
                        note.setStringPlusDisplayLabelPlusTypetype("biographical/historical");
                        note.setDisplayLabel("Interviewer Background and Preparation");
                    }else if("PROCESSING OF INTERVIEW".equals(type)){
                        note.setStringPlusDisplayLabelPlusTypetype("creation/production credits");
                        note.setDisplayLabel("Processing of Interview");
                    }                                   
                   
                   
                    note.setValue(value);
                }else{
                    note.setValue((String)tempValueHolder.get(i));
                }
                
                modsgroup.add(note);
            }

        }

    }
}

private void setDate(List modsgroup, Item currentItem) {
    OriginInfoType origin = ofactory.createOriginInfoType();
    List origins = origin.getPlaceOrPublisherOrDateIssued();
    
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    Map tempMap = new HashMap();
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Date"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
    tempValueHolderSize = tempValueHolder.size();
    
    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) { 
            if("creation".equals(tempQualifierHolder.get(i))){
                DateType creation = ofactory.createDateType();
                creation.setValue((String)tempValueHolder.get(i));
                origins.add(ofactory.createOriginInfoTypeDateCreated(creation));
            }
        }
    }

   
    if(null != origins && !origins.isEmpty()){
        modsgroup.add(origin);
    }
    
}

private void setType(List modsgroup, Item currentItem) {
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    List tempIds = new ArrayList();
    Map source = null;
    Map tempMap = new HashMap();        
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Type"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
    tempIds = (List)tempMap.get("IDS");
    source = (Map)tempMap.get("SOURCE");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) {
            if (tempQualifierHolder.get(i).equals("genre")) {
                GenreType genre = ofactory.createGenreType();
                genre.setValue((String)tempValueHolder.get(i));
                genre.setStringPlusAuthorityPlusLanguageType(source.get(tempIds.get(i)).toString().toLowerCase());
                modsgroup.add(genre);
                
            }
            else {
                TypeOfResourceType typeOfResource = ofactory.createTypeOfResourceType();
                typeOfResource.setValue((String)tempValueHolder.get(i));
                modsgroup.add(typeOfResource);
            }
           
        }
    }
}

private void setLanguage(List modsgroup, Item currentItem) {
    
    /** Language */
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    
    Map tempMap = new HashMap();      
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Language"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {

        for (int i = 0; i < tempValueHolderSize; i++) {

            LanguageType language = ofactory.createLanguageType();
            LanguageType.LanguageTerm languageTerm = ofactory.createLanguageTypeLanguageTerm();
            languageTerm.setValue((String)tempValueHolder.get(i));
            List terms = language.getLanguageTerm();
            terms.add(languageTerm);
            modsgroup.add(language);

        }

    }

}

private void setRelation(List modsgroup, Item currentItem) {
    
    /** Relation */
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    
    Map tempMap = new HashMap();     
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Relation"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");        
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) {

            RelatedItemType relation = ofactory.createRelatedItemType();
            TitleInfoType titleinfoType = ofactory.createTitleInfoType();
            List titles = titleinfoType.getTitleOrSubTitleOrPartNumber();
            titles.add(ofactory.createBaseTitleInfoTypeTitle((String)tempValueHolder.get(i)));
            List mods = relation.getModsGroup();
            mods.add(titleinfoType);
            modsgroup.add(relation);
        }

    }
}

private void setSubjects(List modsgroup, Item currentItem) {
    /** Subjects */
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempIds = new ArrayList();
    Map source = null;
    Map tempMap = new HashMap();     
    tempMap = 
            getdescriptiveMDMap(currentItem.getDescMetadataList("Subject"));
    tempValueHolder = (List)tempMap.get("VALUE");
    tempIds = (List)tempMap.get("IDS");
    source = (Map)tempMap.get("SOURCE");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) {
            SubjectType subject = ofactory.createSubjectType();
            subject.setAuthority(source.get(tempIds.get(i)).toString().toLowerCase());
            List topics = subject.getTopicOrGeographicOrTemporal();
            topics.add(ofactory.createSubjectTypeTopic((String)tempValueHolder.get(i)));
            modsgroup.add(subject);
        }
       

    }
}

private void setAltTitle(List modsgroup, Item currentItem) {
    
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();       
    Map tempMap = new HashMap();
    
    
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("AltTitle"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempValueHolderSize = tempValueHolder.size();
    if (tempValueHolderSize > 0) {
        for (int i = 0; i < tempValueHolderSize; i++) {
            TitleInfoType titleinfoType = ofactory.createTitleInfoType();
            titleinfoType.setTitleInfoTypetype("alternative");
            List titles = titleinfoType.getTitleOrSubTitleOrPartNumber();
            titles.add(ofactory.createBaseTitleInfoTypeTitle((String)tempValueHolder.get(i)));
            modsgroup.add(titleinfoType);
        }
    }
}

private void setRights(List modsgroup, Item currentItem) {
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();       
    Map tempMap = new HashMap();       
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Rights"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempValueHolderSize = tempValueHolder.size();
    if (tempValueHolderSize > 0) {
        for (int i = 0; i < tempValueHolderSize; i++) {
            AccessConditionType access = ofactory.createAccessConditionType();
            List content = access.getContent();
            content.add(tempValueHolder.get(i));
            modsgroup.add(access);
        }
    }
}

private void setAltIdentifier(List modsgroup, Item currentItem) {
    
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    Map tempMap = new HashMap();        
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("AltIdentifier"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) {
           
                IdentifierType identifier = ofactory.createIdentifierType();
                identifier.setType((String)tempQualifierHolder.get(i));
                identifier.setValue((String)tempValueHolder.get(i));
                modsgroup.add(identifier);
            
           
        }
    }
    
}

private void setName(List modsgroup, Item currentItem) {
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    Map tempMap = new HashMap();                
    
    tempMap = 
            (Map)getdescriptiveMDMap(currentItem.getDescMetadataList("Name"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        
                        
        for (int i = 0; i < tempValueHolderSize; i++) {
            NameType name = ofactory.createNameType();
            List contents = name.getNamePartOrDisplayFormOrAffiliation();
            NamePartType namePart = ofactory.createNamePartType();
            namePart.setValue((String)tempValueHolder.get(i));
            contents.add(ofactory.createNameTypeNamePart(namePart));
            RoleType role = ofactory.createRoleType();
            List terms = role.getRoleTerm();
            RoleType.RoleTerm roleTerm = ofactory.createRoleTypeRoleTerm();
            roleTerm.setValue((String)tempQualifierHolder.get(i));              
            roleTerm.setType(CodeOrText.TEXT);               
            terms.add(roleTerm);
            contents.add(ofactory.createNameTypeRole(role));
            modsgroup.add(name);          

        }
      

    }
}

private void setSource(List modsgroup, Item currentItem)  {
    /** SOURCE */
    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    Map tempMap = new HashMap();
    tempMap = getdescriptiveMDMap(currentItem.getDescMetadataList("Source"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempValueHolderSize = tempValueHolder.size();

    if (tempValueHolderSize > 0) {
        for (int i = 0; i < tempValueHolderSize; i++) {
            RelatedItemType relation = ofactory.createRelatedItemType();
            TitleInfoType titleinfoType = ofactory.createTitleInfoType();
            List titles = titleinfoType.getTitleOrSubTitleOrPartNumber();
            titles.add(ofactory.createBaseTitleInfoTypeTitle((String)tempValueHolder.get(i)));
            List mods = relation.getModsGroup();
            mods.add(titleinfoType);
            modsgroup.add(relation);
        }
    }
    }


private void setFormat(List modsgroup, Item currentItem)  {

    int tempValueHolderSize = 0;
    List tempValueHolder = new ArrayList();
    List tempQualifierHolder = new ArrayList();
    
    Map tempMap = new HashMap();

    tempMap = getdescriptiveMDMap(currentItem.getDescMetadataList("Format"));
    tempValueHolder = (ArrayList)tempMap.get("VALUE");
    tempQualifierHolder = (ArrayList)tempMap.get("QUALIFIER");
   
    tempValueHolderSize = tempValueHolder.size();
    PhysicalDescriptionDefinition physicalDesc = ofactory.createPhysicalDescriptionDefinition();
    List extents  = physicalDesc.getFormOrReformattingQualityOrInternetMediaDefinition();

    if (tempValueHolderSize > 0) {
        for (int i = 0; i < tempValueHolderSize; i++) {
            if ("dimensions".equals(tempQualifierHolder.get(i)) || "extent".equals(tempQualifierHolder.get(i)) ) {                               
                
                extents.add(ofactory.createPhysicalDescriptionDefinitionExtent((String)tempValueHolder.get(i)));
            }else{
                // if unqualified data like in oralhistory
                 extents.add(ofactory.createPhysicalDescriptionDefinitionExtent((String)tempValueHolder.get(i)));
            }
        }
    }
    
    if (null != extents && !extents.isEmpty()) {
        modsgroup.add(physicalDesc);
    }
}

private void setConstituents(List modsgroup, Item currentItem) {

    List<ContentDisplayWrapper> tempValueHolder = (ArrayList<ContentDisplayWrapper>)currentItem.getSessionContent();        
    int tempValueHolderSize = tempValueHolder.size();
    String prefixUrl = "https://wowza.library.ucla.edu:443/dlp/definst/mp3:";
    String suffixUrl= "/playlist.m3u8";

    if (tempValueHolderSize > 0) {
        
        for (int i = 0; i < tempValueHolderSize; i++) {
            ContentDisplayWrapper content = (ContentDisplayWrapper)tempValueHolder.get(i);
            RelatedItemDefinition relation = ofactory.createRelatedItemDefinition();
            relation.setTypeRelatedItemDefinition("constituent");
           // relation.setID(content.getDisplayArk());
            relation.setHref(prefixUrl+currentItem.getWebAppName()+"/"+content.getDisplayFileName().replace("ram","mp3")+suffixUrl);
            TitleInfoDefinition titleinfoDefinition = ofactory.createTitleInfoDefinition();
            List titles = titleinfoDefinition.getTitleOrSubTitleOrPartNumber();
            TitleInfoDefinition titleinfo = ofactory.createTitleInfoDefinition();
            titleinfo.s
            titles.add((content.getDisplayTitle()));
            List mods = relation.getModsGroup();
            mods.add(titleinfoDefinition);
            PartDefinition partDefinition = ofactory.createPartDefinition();
            partDefinition.setDefinition("session_audio");
            partDefinition.setOrder(BigInteger.valueOf(content.getItemSequence()));
            mods.add(partDefinition);
            TableOfContentsDefinition tableOfContentsDefinition = ofactory.createTableOfContentsDefinition();
            tableOfContentsDefinition.setValue(content.getDisplayTableOfContent());
            mods.add(tableOfContentsDefinition);
           IdentifierDefinition identifierDefinition = ofactory.createIdentifierDefinition();
            identifierDefinition.setValue(content.getDisplayArk());
            mods.add(identifierDefinition);
            if(null != content.getTranscriptFileName()){
                LocationDefinition location = ofactory.createLocationDefinition();
                UrlDefinition url = ofactory.createUrlDefinition();
                url.setUsage("timed log");
                url.setValue("http://digital2.library.ucla.edu/dlcontent/"+currentItem.getWebAppName()+"/xml/"+content.getTranscriptFileName());
                List urls = location.getUrl();                
                urls.add(url);
                mods.add(location);
            }
            
            modsgroup.add(relation);
        }

    }


}

private void setSeries(List modsgroup, Item currentItem) {

/**
 * -<mods:relatedItem type="series">
*-      <mods:titleInfo>
*           <mods:title>African American Artists of Los Angeles </mods:title>
*           </mods:titleInfo>
*           <mods:identifier>21198/zz000905n9</mods:identifier>
*           <mods:abstract>
The Suburban Chinatown series focuses on political and business leaders in the San Gabriel Valley who came to the U.S. in the post-1965 wave of Asian immigration after the Immigration and Nationality Act of 1965 abolished the quota system based on national origins. The series was undertaken as a collaborative effort between the UCLA Librarys Center for Oral History Research and the American East Asian Cultural and Educational Foundation (AEACEF). AEACEF recommended the majority of the individuals interviewed and introduced the interviewer to the narrators. Many of the narrators are also featured in the AEACEFs book Thirty Years of Chinese American Immigration in Southern California.
</mods:abstract>
*   </mods:relatedItem>
 */
 
 RelatedItemType relation = ofactory.createRelatedItemType();
    relation.setRelatedItemType("series");
 TitleInfoType titleinfoType = ofactory.createTitleInfoType();
 List titles = titleinfoType.getTitleOrSubTitleOrPartNumber();
 titles.add(ofactory.createBaseTitleInfoTypeTitle((String)currentItem.getSeries_title()));
 List mods = relation.getModsGroup();
 mods.add(titleinfoType);
    IdentifierType identifierType = ofactory.createIdentifierType();
     identifierType.setValue(currentItem.getSeries_ark());
     mods.add(identifierType);
     if(null != currentItem.getSeries_abstract() && currentItem.getSeries_abstract().trim().length() > 0){
         AbstractType abs = ofactory.createAbstractType();
         abs.setValue(currentItem.getSeries_abstract().trim());
         mods.add(abs);
     }
 modsgroup.add(relation);
 
}
}
}
