package com.jayesh.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jayesh.request.SearchRequest;
import com.jayesh.response.SearchResponse;
import com.jayesh.service.impl.ReportsServiceImpl;

@RestController
public class ReportsController {

	@Autowired
	private ReportsServiceImpl reportsServiceImpl;
	
	@GetMapping("/plan/names")
	public ResponseEntity<List<String>> getPlans(){
		return new ResponseEntity<>(reportsServiceImpl.getUniquePlanName(), HttpStatus.OK);
	}
	
	@GetMapping("/plan/statuses")
	public ResponseEntity<List<String>> getPlanStatuses(){
		return new ResponseEntity<>(reportsServiceImpl.getUniquePlanStatus(), HttpStatus.OK);
	}
	
	@PostMapping("/plan/report")
	public ResponseEntity<List<SearchResponse>> getReport(@RequestBody SearchRequest request){
		return new ResponseEntity<>(reportsServiceImpl.search(request),HttpStatus.OK);
	}
	
	@GetMapping("/plan/export/excel")
	public void exportToExcel(HttpServletResponse response) {
		
		response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xls";
        response.setHeader(headerKey, headerValue);
        
        reportsServiceImpl.generateExcel(response);
	}
	
	@GetMapping("/plan/export/pdf")
	public void exportToPdf(HttpServletResponse response) {
		response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        
        reportsServiceImpl.generatePdf(response);
	}
}
