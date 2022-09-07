package com.jayesh.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.jayesh.entity.EligibilityDtls;
import com.jayesh.repo.EligibilityDtlsRepo;

@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private EligibilityDtlsRepo repo;
	@Override
	public void run(ApplicationArguments args) throws Exception {

		EligibilityDtls e1=new EligibilityDtls();
		e1.setEligId(2);
		e1.setName("John");
		e1.setEmail("John@abc.com");
		e1.setMobile(987654321);
		e1.setGender("M");
		e1.setPlanName("SNAP");
		e1.setSsn(852369741);
		
		repo.save(e1);
	}

}
