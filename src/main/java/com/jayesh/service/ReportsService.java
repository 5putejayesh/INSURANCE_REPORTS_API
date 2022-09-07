package com.jayesh.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jayesh.request.SearchRequest;
import com.jayesh.response.SearchResponse;

public interface ReportsService {
	
	public List<String> getUniquePlanName();
	public List<String> getUniquePlanStatus();
	public List<SearchResponse> search(SearchRequest request);
	public void generateExcel(HttpServletResponse response);
	public void generatePdf(HttpServletResponse response);

}
