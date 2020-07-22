package com.tost.healthapp.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tost.healthapp.controllers.RxController;
import com.tost.healthapp.domain.Rx;
import com.tost.healthapp.repositories.RxDAO;

@Service
public class RxServiceImpl implements RxService {
	
	final static Logger logger = LoggerFactory.getLogger(RxServiceImpl.class);

	@Autowired private RxDAO rxDAO;
	
	@Override
	public List<Rx> findByDoctorId(int id) {
		return rxDAO.findByDoctorId(id);
	}

	@Override
	public void save(Rx rx) {
		rxDAO.save(rx);
	}

	@Override
	public List<Rx> findByPatientId(int id) {
		return rxDAO.findByUserId(id);
	}

}
