package com.jayesh.service.impl;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.jayesh.entity.EligibilityDtls;
import com.jayesh.repo.EligibilityDtlsRepo;
import com.jayesh.request.SearchRequest;
import com.jayesh.response.SearchResponse;
import com.jayesh.service.ReportsService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportsServiceImpl implements ReportsService {

	@Autowired
	private EligibilityDtlsRepo eligibilityDtlsRepo;

	@Override
	public List<String> getUniquePlanName() {

		return eligibilityDtlsRepo.findPlanNames();

		/*
		 * execution time will be more for below approach return
		 * eligibilityDtlsRepo.findAll().stream().map(e->e.getPlanName()).distinct().
		 * collect(Collectors.toList());
		 */

	}

	@Override
	public List<String> getUniquePlanStatus() {

		return eligibilityDtlsRepo.findPlanStatuses();

		/*
		 * execution time will be more for below approach return
		 * eligibilityDtlsRepo.findAll().stream().map(e->e.getPlanStatus()).distinct().
		 * collect(Collectors.toList());
		 */
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {

		EligibilityDtls queryBuilder = new EligibilityDtls();

		if (StringUtils.isNotBlank(request.getPlanName())) {
			queryBuilder.setPlanName(request.getPlanName());
		}

		if (StringUtils.isNotBlank(request.getPlanStatus())) {
			queryBuilder.setPlanStatus(request.getPlanStatus());
		}

		if (request.getPlanStartDate() != null) {

			queryBuilder.setPlanStartDate(request.getPlanStartDate());
		}

		if (request.getPlanEndDate() != null) {

			queryBuilder.setPlanEndDate(request.getPlanEndDate());
		}

		Example<EligibilityDtls> example = Example.of(queryBuilder);

		List<EligibilityDtls> entities = eligibilityDtlsRepo.findAll(example);

		return entities.stream().map(e -> mapToDto(e)).collect(Collectors.toList());

	}

	@Override
	public void generateExcel(HttpServletResponse response) {

		List<EligibilityDtls> entities = eligibilityDtlsRepo.findAll();

		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		HSSFRow headerRow = sheet.createRow(0);

		writeExcelHeader(headerRow);
		writeExcelData(entities, sheet);

		try {
			ServletOutputStream outputStream = response.getOutputStream();
			workBook.write(outputStream);
			workBook.close();
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void writeExcelData(List<EligibilityDtls> entities, HSSFSheet sheet) {
		
		/*
		 * with Java 8
		 * AtomicInteger counter = new AtomicInteger(1);

		entities.forEach(e -> {
			//HSSFRow dataRow = sheet.createRow(counter.getAndIncrement());
			int i=1;
			HSSFRow dataRow = sheet.createRow(i++);
			dataRow.createCell(0).setCellValue(e.getName());
			dataRow.createCell(1).setCellValue(e.getEmail());
			dataRow.createCell(2).setCellValue(e.getMobile());
			dataRow.createCell(3).setCellValue(e.getGender());
			dataRow.createCell(4).setCellValue(e.getSsn());
		});
		*/
		int i=1;
		for(EligibilityDtls e :entities) {
			HSSFRow dataRow = sheet.createRow(i++);
			dataRow.createCell(0).setCellValue(e.getName());
			dataRow.createCell(1).setCellValue(e.getEmail());
			dataRow.createCell(2).setCellValue(e.getMobile());
			dataRow.createCell(3).setCellValue(e.getGender());
			dataRow.createCell(4).setCellValue(e.getSsn());
		}
	}

	private void writeExcelHeader(HSSFRow headerRow) {
		headerRow.createCell(0).setCellValue("Cust Name");
		headerRow.createCell(1).setCellValue("Email");
		headerRow.createCell(2).setCellValue("Mobile Number");
		headerRow.createCell(3).setCellValue("Gender");
		headerRow.createCell(4).setCellValue("SSN");
	}

	@Override
	public void generatePdf(HttpServletResponse response) {

		List<EligibilityDtls> entities = eligibilityDtlsRepo.findAll();
		
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, response.getOutputStream());

			document.open();
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setSize(18);
			font.setColor(Color.BLUE);

			Paragraph p = createPpdfPara(font);

			PdfPTable table = createPdfTable();

			writePdfHeader(table);
			
			writePdfData(entities, table);

			document.add(p);
			document.add(table);
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PdfPTable createPdfTable() {
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);
		return table;
	}

	private Paragraph createPpdfPara(Font font) {
		Paragraph p = new Paragraph("Eligibility Reports", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		return p;
	}

	private void writePdfData(List<EligibilityDtls> entities, PdfPTable table) {
		entities.forEach(e->{
			table.addCell(e.getName());
			table.addCell(e.getEmail());
			table.addCell(String.valueOf(e.getMobile()));
			table.addCell(e.getGender());
			table.addCell(String.valueOf(e.getSsn()));
		});
	}

	private void writePdfHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA);
		headerFont.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Name", headerFont));

		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", headerFont));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Mobile", headerFont));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", headerFont));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN", headerFont));
		table.addCell(cell);
	}

	private SearchResponse mapToDto(EligibilityDtls dtls) {
		SearchResponse res = new SearchResponse();
		BeanUtils.copyProperties(dtls, res);
		return res;
	}

}
