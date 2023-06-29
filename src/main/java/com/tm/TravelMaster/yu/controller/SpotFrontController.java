package com.tm.TravelMaster.yu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tm.TravelMaster.yu.model.Spot;
import com.tm.TravelMaster.yu.service.SpotService;

@Controller
public class SpotFrontController {

	@Autowired
	private SpotService spotService;

	@GetMapping("/toSpot")
	public String toSpot() {
		return "yu/Index";
	}
	
	@GetMapping("/allSpot")
    public String query(Model model) {
        List<Spot> spots = spotService.findAll();
        model.addAttribute("spots", spots);
        return "yu/AllSpot";
    }
	
}
