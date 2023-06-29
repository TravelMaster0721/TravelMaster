package com.tm.TravelMaster.yu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tm.TravelMaster.yu.model.Spot;
import com.tm.TravelMaster.yu.service.SpotService;

@Controller
@RequestMapping("/spotList")
public class SpotController {
	
	@Autowired
	private SpotService spotService;
	
	@GetMapping("")
    public String query(Model model) {
        List<Spot> spots = spotService.findAll();
        model.addAttribute("spots", spots);
        return "yu/Result";
    }

    @GetMapping("/insert")
    public String insert(Model model) {
	    model.addAttribute("cityNames", spotService.getAllCityNames());
	    model.addAttribute("spotTypes", spotService.getAllSpotTypes());
        return "yu/Insert";
    }
 
    @GetMapping("/update")
    public String update(@RequestParam("id") String id, Model model) {
        Spot spots = spotService.findById(Integer.parseInt(id));
	    model.addAttribute("cityNames", spotService.getAllCityNames());
	    model.addAttribute("spotTypes", spotService.getAllSpotTypes());
        model.addAttribute("spots", spots);
        return "yu/Insert";
    }
    
    @PostMapping("/doAction")
    public String doAction(@ModelAttribute("spots") Spot spots, @RequestParam("action") String action) {
    	if(action.equals("doInsert")) {
    		spotService.insert(spots);
    	}else if(action.equals("doUpdate")) {
    		spotService.update(spots);
    	}
    	return "redirect:/spotList";
    }
 
    @GetMapping("/delete")
    public String delete(@RequestParam("spotNo") Integer spotNo) {
        spotService.deleteById(spotNo);
        return "redirect:/spotList";
    }
    
    
    @GetMapping("/toSpot")
	public String toSpot() {
		return "yu/Index";
	}
	
	@GetMapping("/allSpot")
    public String showAllSpot(Model model) {
        List<Spot> spots = spotService.findAll();
        model.addAttribute("spots", spots);
        return "yu/AllSpot";
    }
}

