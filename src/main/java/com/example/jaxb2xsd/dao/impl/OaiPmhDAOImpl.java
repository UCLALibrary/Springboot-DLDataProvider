package com.example.jaxb2xsd.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.jaxb2xsd.dao.OaiPmhDAO;
import com.example.jaxb2xsd.mapper.ProjectMapper;
import com.example.jaxb2xsd.model.ContentDisplayWrapper;
import com.example.jaxb2xsd.model.DescControlValue;
import com.example.jaxb2xsd.model.Item;
import com.example.jaxb2xsd.model.Project;
import com.example.jaxb2xsd.util.Constants;

@Repository
public class OaiPmhDAOImpl implements OaiPmhDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Project> getOAISets() {
		// TODO Auto-generated method stub
		String GET_OAI_PROJECTS = "SELECT * FROM projects WHERE oai_flag = 'yes' and auto_publish_flag = 'yes' ORDER BY project_title ASC";
		List<Project> projects = jdbcTemplate.query(GET_OAI_PROJECTS, new ProjectMapper());
		return projects;
	}

	@Override
	public List<Item> getRecordForOai(String ark) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append(
				"select a.webapp_name from projects a, project_items b where a.projectid_pk=b.projectid_fk and b.item_ark=?");

		String projectAppName = jdbcTemplate.queryForObject(builder.toString(), String.class, new Object[] { ark });

		if (projectAppName != null && projectAppName.equals("cdli")) {
			List<String> arkHolder = new ArrayList<>();
			arkHolder.add(ark);
			List<Item> arks = getCdliRecordForOAI(arkHolder);
			return arks;
		}

		builder = new StringBuilder();

		builder.append("SELECT ");
		if (null != projectAppName && "oralhistory".equals(projectAppName)) {
			builder.append(
					"series.node_title as series_title, series.item_ark as series_ark, series_values.desc_value as series_abstract, ");
		}
		builder.append(
				"projects.webapp_name, project_items.item_ark, project_items.divid_pk, core_desc_terms.DESC_TERM term_label, desc_terms.core_desc_termid_fk, desc_terms.control_value_flag, desc_terms.qualifier_flag, core_desc_control_values.core_desc_cvid_pk, core_desc_control_values.core_desc_cv,");
		builder.append(
				"core_desc_control_values.core_desc_cv_source, core_desc_qualifiers.desc_qualifier, core_desc_qualifiers.core_desc_qualifierid_pk, desc_values.desc_value, desc_values.DESC_VALUEID_PK, project_items.LAST_EDIT_DATE, vcf.THUMBNAIL_LOCATION  ");
		builder.append(
				"FROM desc_values, project_items, desc_terms,core_desc_terms, desc_qualifiers, core_desc_qualifiers, desc_control_values, core_desc_control_values, projects, (SELECT div_id, thumbnail_location FROM v_content_files WHERE content_type = 'Image' and fs1 = 1) vcf  ");
		if (null != projectAppName && "oralhistory".equals(projectAppName)) {
			builder.append(", project_items series, desc_values series_values ");
		}
		builder.append("WHERE project_items.item_ark = '" + ark + "' ");
		builder.append("and projects.PROJECTID_PK = project_items.PROJECTID_FK ");
		builder.append("and projects.AUTO_PUBLISH_FLAG = 'yes' ");
		builder.append("and projects.OAI_FLAG = 'yes' ");
		builder.append("AND desc_values.divid_fk = project_items.divid_pk ");
		builder.append("AND project_items.statusid_fk IN (2,10)  ");
		builder.append("AND desc_values.desc_termid_fk = desc_terms.desc_termid_pk ");
		builder.append("AND desc_terms.core_desc_termid_fk = core_desc_terms.CORE_DESC_TERMID_PK ");
		builder.append("AND desc_values.desc_qualifierid_fk = desc_qualifiers.desc_qualifierid_pk(+) ");
		builder.append(
				"AND desc_qualifiers.core_desc_qualifierid_fk = core_desc_qualifiers.core_desc_qualifierid_pk(+) ");
		builder.append("AND desc_values.desc_cvid_fk = desc_control_values.desc_cvid_pk(+) ");
		builder.append("AND desc_control_values.core_desc_cvid_fk = core_desc_control_values.core_desc_cvid_pk(+) ");
		// THUMBNAIL URL
		builder.append("AND project_items.divid_pk = vcf.DIV_ID(+) ");

		if (null != projectAppName && "oralhistory".equals(projectAppName)) {
			builder.append("AND series.divid_pk = project_items.parent_divid ");
			builder.append("AND series_values.divid_fk = series.divid_pk ");
			builder.append("AND series_values.desc_termid_fk = " + Constants.SERIES_TERMID);
			builder.append("AND series_values.desc_qualifierid_fk = " + Constants.SERIES_QUALIFIERID + " ");
		}
		return jdbcTemplate.query(builder.toString(), new ItemMapExtractor());

	}

	public List<Item> getCdliRecordForOAI(List<String> arkHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	private  class ItemMapExtractor implements ResultSetExtractor<List<Item>> {
		@Override
		public List<Item> extractData(ResultSet rs) throws SQLException {
			if (null != rs) {
				List<Item> items = new ArrayList<>();
				Map<String, Item> ids = new HashMap<String, Item>();
				while (rs.next()) {
					
					if(ids.containsKey((String)rs.getString("ITEM_ARK"))){
                        Item liteItem = ids.get((String)rs.getString("ITEM_ARK"));
                       DescControlValue controlValue = new DescControlValue();
                       if(null != rs.getString("CONTROL_VALUE_FLAG")){
                           controlValue.setControlValueFlag((String)rs.getString("CONTROL_VALUE_FLAG"));
                       }
                       if(null != rs.getString("CORE_DESC_CV")){
                           controlValue.setCoreDescControlValue((String)rs.getString("CORE_DESC_CV"));
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_CVID_PK")){
                           controlValue.setCoreDescControlValueId(rs.getBigDecimal("CORE_DESC_CVID_PK").intValue());
                       }
                       if(null != rs.getString("CORE_DESC_CV_SOURCE")){
                           controlValue.setCoreDescControlValueSource((String)rs.getString("CORE_DESC_CV_SOURCE"));
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_QUALIFIERID_PK")){
                           controlValue.setCoreDescQualifierId(rs.getBigDecimal("CORE_DESC_QUALIFIERID_PK").intValue());
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_TERMID_FK")){
                           controlValue.setCoreDescTermId(rs.getBigDecimal("CORE_DESC_TERMID_FK").intValue());
                       }
                       if(null != rs.getString("DESC_QUALIFIER")){
                           controlValue.setDescQualifier((String)rs.getString("DESC_QUALIFIER"));
                       }
                       if(null != rs.getString("DESC_VALUE")){
                           controlValue.setDescValue((String)rs.getString("DESC_VALUE"));
                       }
                       if(null != rs.getBigDecimal("DESC_VALUEID_PK")){
                           controlValue.setDescValueId(rs.getBigDecimal("DESC_VALUEID_PK").intValue());
                       }
                       if(null != rs.getString("QUALIFIER_FLAG")){
                           controlValue.setQualifierFlag((String)rs.getString("QUALIFIER_FLAG"));
                       }
                       if(null != rs.getString("TERM_LABEL")){
                           controlValue.setTermLabel((String)rs.getString("TERM_LABEL"));
                       }
                       liteItem.getDescValues().add(controlValue);

                   } else {
                       Item liteItem = new Item();
                       liteItem.setArk((String)rs.getString("ITEM_ARK"));
                       
                       liteItem.setWebAppName((String)rs.getString("WEBAPP_NAME"));
                       liteItem.setSeries_title((String)rs.getString("SERIES_TITLE"));
                       liteItem.setSeries_ark((String)rs.getString("SERIES_ARK"));
                       if(null != rs.getString("SERIES_ABSTRACT")){
                           liteItem.setSeries_abstract((String)rs.getString("SERIES_ABSTRACT"));
                       }
                       if(null != rs.getString("THUMBNAIL_LOCATION")){
                       liteItem.setThumbnailURL((String)rs.getString("THUMBNAIL_LOCATION"));
                       }
                       liteItem.setLastEditDate(new Date(rs.getTimestamp("LAST_EDIT_DATE").getTime()));
                       liteItem.setSessionContent(getSessions(rs.getBigDecimal("DIVID_PK").longValue()));
                       List<DescControlValue> descValues =  new ArrayList<>();
                       DescControlValue controlValue = new DescControlValue();
                       if(null != rs.getString("CONTROL_VALUE_FLAG")){
                           controlValue.setControlValueFlag((String)rs.getString("CONTROL_VALUE_FLAG"));
                       }
                       if(null != rs.getString("CORE_DESC_CV")){
                           controlValue.setCoreDescControlValue((String)rs.getString("CORE_DESC_CV"));
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_CVID_PK")){
                           controlValue.setCoreDescControlValueId(rs.getBigDecimal("CORE_DESC_CVID_PK").longValue());
                       }
                       if(null != rs.getString("CORE_DESC_CV_SOURCE")){
                           controlValue.setCoreDescControlValueSource((String)rs.getString("CORE_DESC_CV_SOURCE"));
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_QUALIFIERID_PK")){
                           controlValue.setCoreDescQualifierId(rs.getBigDecimal("CORE_DESC_QUALIFIERID_PK").longValue());
                       }
                       if(null != rs.getBigDecimal("CORE_DESC_TERMID_FK")){
                           controlValue.setCoreDescTermId(rs.getBigDecimal("CORE_DESC_TERMID_FK").longValue());
                       }
                       if(null != rs.getString("DESC_QUALIFIER")){
                           controlValue.setDescQualifier((String)rs.getString("DESC_QUALIFIER"));
                       }
                       if(null != rs.getString("DESC_VALUE")){
                           controlValue.setDescValue((String)rs.getString("DESC_VALUE"));
                       }
                       if(null != rs.getBigDecimal("DESC_VALUEID_PK")){
                           controlValue.setDescValueId(rs.getBigDecimal("DESC_VALUEID_PK").intValue());
                       }
                       if(null != rs.getString("QUALIFIER_FLAG")){
                           controlValue.setQualifierFlag(rs.getString("QUALIFIER_FLAG"));
                       }
                       if(null != rs.getString("TERM_LABEL")){
                           controlValue.setTermLabel(rs.getString("TERM_LABEL"));
                       }

                       descValues.add(controlValue);
                       liteItem.setDescValues(descValues);
                       ids.put(rs.getString("ITEM_ARK"),liteItem);
                   }

                  


               }
              Set<String> arkIds = ids.keySet();
              if(null != arkIds && !arkIds.isEmpty()){
                  Iterator<String> arkIterator = arkIds.iterator();
                  while(arkIterator.hasNext()){
                      String ark = arkIterator.next();
                      items.add(ids.get(ark));
                  }
              }

				
				return items;
			}
			return null;
		}
	}

	public  List<ContentDisplayWrapper> getSessions(long parent_divid) {
		List<ContentDisplayWrapper> results = null;
		   
		     
		   
		    StringBuilder builder = new StringBuilder();
		    
		    // this fetches any children with or without mp3 attached to it
		    
		   /* builder.append("SELECT node_title, item_sequence, item_ark, vcf.file_location, vcf.file_name, descvalues.text_value ");
		    builder.append("FROM project_items, (select divid_fk, file_location, file_name from content_files where content_type = 'Audio' and content_files.file_use = 'Submaster') vcf, ");
		    builder.append("(select text_value, item_id from vw_item_metadata where desc_term_label = 'Description'  AND qualifier_label = 'tableOfContents' ) descvalues ");
		    builder.append("WHERE parent_divid = "+parent_divid+" and vcf.divid_fk(+) = project_items.divid_pk ");
		    builder.append("AND descvalues.item_id(+) = project_items.divid_pk AND project_items.statusid_fk IN (2,10) ");*/
		    
		    // for the first phase we will focus only on audio content
		     builder.append("SELECT divid_pk, node_title, item_sequence, item_ark, vcf.file_location, vcf.file_name, descvalues.text_value ");
		     builder.append("FROM project_items, content_files vcf, vw_item_metadata descvalues ");
		     builder.append("WHERE parent_divid = "+parent_divid+" and vcf.divid_fk(+) = project_items.divid_pk and vcf.content_type = 'Audio' and vcf.file_use = 'Submaster'  ");
		     builder.append("AND descvalues.item_id(+) = project_items.divid_pk AND project_items.statusid_fk IN (2,10) and desc_term_label = 'Description'  AND qualifier_label = 'tableOfContents' ");

		     results =jdbcTemplate.query(builder.toString(), new ContentDisplayExtractor());

		    
		   
		    
		    return results;
	}
	
	private class ContentDisplayExtractor implements ResultSetExtractor<List<ContentDisplayWrapper>>{

		@Override
		public List<ContentDisplayWrapper> extractData(ResultSet rs) throws SQLException, DataAccessException {
			// TODO Auto-generated method stub
			List<ContentDisplayWrapper> sessions = null;
			 String transcription_file_name = null;
			    if(null != rs){
			         sessions = new ArrayList<>();
			       
			       
			        while (rs.next()) {
			            
			            
			            ContentDisplayWrapper liteItem = new ContentDisplayWrapper();
			            liteItem.setDisplayTitle(rs.getString("NODE_TITLE"));
			            liteItem.setItemSequence(rs.getBigDecimal("ITEM_SEQUENCE").intValue());
			            liteItem.setDisplayArk(rs.getString("ITEM_ARK"));
			            liteItem.setDisplayLocation(rs.getString("FILE_LOCATION"));
			            liteItem.setDisplayFileName(rs.getString("FILE_NAME"));
			            liteItem.setDisplayTableOfContent(rs.getString("TEXT_VALUE"));
			            transcription_file_name = getTimelog((rs.getBigDecimal("DIVID_PK")).longValue());
			            if(null != transcription_file_name){
			                liteItem.setTranscriptionFileName(transcription_file_name);
			            }
			           
			            sessions.add(liteItem);
			        }
			    
			    }
			    return sessions;
		}

		private String getTimelog(long divid_pk) {
			
		        String file_name = null;
		       
		       
		        StringBuilder builder = new StringBuilder();
		        
		       
		        // for the third phase we will focus only on audio content and its transcripts
		         builder.append("SELECT vcf.file_name ");
		         builder.append("FROM content_files vcf ");
		         builder.append("WHERE vcf.divid_fk = "+divid_pk+" and vcf.mime_type = 'text/xml' and vcf.file_use = 'Master'  ");
		         

		         file_name = jdbcTemplate.queryForObject(builder.toString(), String.class);
		       
		        return file_name;
		}
		
	}

}
