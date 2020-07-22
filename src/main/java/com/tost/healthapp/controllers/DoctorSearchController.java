package com.tost.healthapp.controllers;

import com.tost.healthapp.domain.Doctor;
import com.tost.healthapp.helpers.DoctorInfo;
import com.tost.healthapp.services.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorSearchController {

    final static Logger logger = LoggerFactory.getLogger(DoctorSearchController.class);

    final DoctorService docService;

    public DoctorSearchController(DoctorService docService) {
        this.docService = docService;
    }

    @GetMapping(value="/doctor/count")
    public DoctorInfo getDoctorCount(ModelMap model)
    {
        int count = docService.findCount();
        return new DoctorInfo("All doctors count", count);
    }

    @RequestMapping(value="/doctor/{code}", method= RequestMethod.GET)
    public DoctorInfo getBySpecialityCode(ModelMap model, @PathVariable("code") String code)
    {
        List<Doctor> doctors = docService.findBySpeciality(code);

        if(doctors == null) {
            return new DoctorInfo("No Doctors found!", null);
        }

        return new DoctorInfo("Doctors found", doctors);
    }

    @GetMapping(value="/doctor", produces="application/json")
    public DoctorInfo findAll(ModelMap model)
    {
        List<Doctor> doctors = docService.findAll();
        if(doctors == null) {
            return new DoctorInfo("No Doctors found!", null);
        }

        return new DoctorInfo("Doctors found", doctors);
    }


}
