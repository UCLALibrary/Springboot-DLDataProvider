package com.example.jaxb2xsd.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jaxb2xsd.service.OaiService;
@Controller
public class GetRecordController {
	
	@Autowired
	private OaiService oaiService;
	
	@RequestMapping(value = "oaipmh", params = "verb=GetRecord")
    public void getRecord(@RequestParam("identifier") String identifier, @RequestParam("metadataPrefix") String metadataprefix,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		oaiService.setIdentifier(identifier);
		oaiService.setMetadataPrefix(metadataprefix);
		IOUtils.write(oaiService.getRecord(request.getRequestURL().toString()), response.getOutputStream(),"UTF-8");
        response.flushBuffer();
	}

}
