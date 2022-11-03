package com.jayesh.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ED_ELIG_DTLS")
public class EligibilityDtls {
	
	@Id
	private Integer eligId;			
	private String name;			
	private Integer mobile;			
	private String email;	
	@Column(length = 1)
	private String gender;			
	private Integer ssn;				
	private String planName;		
	private String planStatus;		
	private LocalDate planStartDate;	
	private LocalDate planEndDate;	
	private LocalDate createDate;		
	private LocalDate updateDate;		
	private String createdBy;		
	private String updatedBy; 		


}
