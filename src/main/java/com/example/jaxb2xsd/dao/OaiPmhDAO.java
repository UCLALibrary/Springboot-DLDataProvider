package com.example.jaxb2xsd.dao;

import java.util.List;

import com.example.jaxb2xsd.model.Item;
import com.example.jaxb2xsd.model.Project;

public interface OaiPmhDAO {
	
	public List<Project> getOAISets();
	
	public List<Item> getRecordForOai(String ark);
}
