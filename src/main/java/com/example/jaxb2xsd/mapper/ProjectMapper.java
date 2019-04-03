package com.example.jaxb2xsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.jaxb2xsd.model.Project;

public class ProjectMapper implements RowMapper<Project> {

	@Override
	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Project project = new Project();
		project.setTitle(rs.getString("project_title"));
		project.setWebappName(rs.getString("webapp_name"));
		
		return project;
	}

}
