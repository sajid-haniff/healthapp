package com.tost.healthapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tost.healthapp.domain.Doctor;
import com.tost.healthapp.domain.Rx;
import com.tost.healthapp.domain.User;
import com.tost.healthapp.dto.RxDTO;
import com.tost.healthapp.exceptions.UserNotFoundException;
import com.tost.healthapp.services.DoctorService;
import com.tost.healthapp.services.RxService;
import com.tost.healthapp.services.UserService;

@RestController
public class RxController {
	final static Logger logger = LoggerFactory.getLogger(RxController.class);
	
	final
	RxService rxService;
	
	final
	UserService userService;
	
	final
	DoctorService docService;

	public RxController(RxService rxService, UserService userService, DoctorService docService) {
		this.rxService = rxService;
		this.userService = userService;
		this.docService = docService;
	}


	@GetMapping(value="/rx", produces="application/json")
	public List<RxDTO> getRx(ModelMap model) {		
		List<Rx> rxList = null;
		User user = getUser();
		if(user.getRole() == 1) {
			rxList = rxService.findByDoctorId(user.getId());
		} else {
			rxList = rxService.findByPatientId(user.getId());
		}
		List<RxDTO> rxDTOList = new ArrayList<RxDTO>();
		for(Rx rx: rxList) {
			RxDTO rxDTO = new RxDTO();
			rxDTO.setMedicine(rx.getMedicine());
			rxDTO.setSymptoms(rx.getSymptoms());
			rxDTO.setPatientName(rx.getUser().getFirstname());
			rxDTOList.add(rxDTO);
		}
		return rxDTOList;
	}
	
	@PostMapping(value="/rx/new", produces="application/json")
	public Rx createRx(ModelMap model, @RequestBody RxDTO reqRx) {
		System.out.println("Inside createRx......");
		Rx rx = new Rx();
		rx.setMedicine(reqRx.getMedicine());
		rx.setSymptoms(reqRx.getSymptoms());
		User patient = null;
		try {
			patient = userService.getByEmail(reqRx.getPatientId());
		} catch (UserNotFoundException e) {
		}
		rx.setUser(patient);
		Doctor doctor = docService.findByUserEmailAddress(getUserEmailAddress());
		rx.setDoctor(doctor);
		rxService.save(rx);
		return rx;
	}
	
	private User getUser() {
		String userEmailAddress = getUserEmailAddress();
		User user = null;
		try {
			user = userService.doesUserExist(userEmailAddress);
		} catch (UserNotFoundException e) {
		}
		return user;
	}
	
	private String getUserEmailAddress() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUsername(); 
	}
		
}
