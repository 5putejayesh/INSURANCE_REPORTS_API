package com.jayesh.request;

import java.time.LocalDate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api
public class SearchRequest {

	@ApiModelProperty(value = "Plan Name",allowEmptyValue = true)
	private String planName;
	@ApiModelProperty(value="Plan Status",allowEmptyValue = true)
	private String planStatus;
	@ApiModelProperty(value = "Plan Start Date",allowEmptyValue = true,example = "YYYY-MM-DD")
	private LocalDate planStartDate;
	@ApiModelProperty(value = "Plan End Date",allowEmptyValue = true,example = "YYYY-MM-DD")
	private LocalDate planEndDate;
}
