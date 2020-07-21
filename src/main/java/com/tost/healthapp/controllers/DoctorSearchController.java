package com.tost.healthapp.controllers;

import com.tost.healthapp.helpers.DoctorInfo;
import com.tost.healthapp.services.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorSearchController {

    final static Logger logger = LoggerFactory.getLogger(DoctorSearchController.class);

    @Autowired
    DoctorService docService;

    @GetMapping(value="/doctor/count")
    public DoctorInfo getDoctorCount(ModelMap model)
    {
        int count = docService.findCount();
        return new DoctorInfo("All doctors count", count);
    }
}
