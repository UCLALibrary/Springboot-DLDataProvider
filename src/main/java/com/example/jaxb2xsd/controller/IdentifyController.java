package com.example.jaxb2xsd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jaxb2xsd.service.OaiService;

@Controller
public class IdentifyController {
	
	@Autowired
	private OaiService oaiService;
	
	@RequestMapping(value = "oaipmh", params = "verb=identify")
    public void identify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		IOUtils.write(oaiService.identify(request.getRequestURL().toString()), response.getOutputStream(),"UTF-8");
        response.flushBuffer();
		
		
	}

	

}
