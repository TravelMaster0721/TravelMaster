package com.tm.TravelMaster.yu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.TravelMaster.yu.model.Spot;
import com.tm.TravelMaster.yu.model.SpotRepository;

@Service	
public class SpotService {
	
	@Autowired
	private SpotRepository sRepo;
	
	public Spot insert(Spot spot) {
		return sRepo.save(spot);
	}
	
	public List<String> getAllCityNames() {
		return sRepo.findAllCityNames();
	}
	
	public List<String> getAllSpotTypes() {
		return sRepo.findAllSpotTypes();
	}
	
	public Spot findById(Integer spotNo) {
		Optional<Spot> optional = sRepo.findById(spotNo);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	public List<Spot> findAll(){
		return sRepo.findAll();
	}
	
	@Transactional
	public Spot update(Spot spot) {
	    Optional<Spot> optional = sRepo.findById(spot.getSpotNo());
	    
	    if (optional.isPresent()) {
	        Spot newSpot = optional.get();
	        newSpot.setSpotName(spot.getSpotName());
	        newSpot.setCityName(spot.getCityName());
	        newSpot.setSpotPrice(spot.getSpotPrice());
	        newSpot.setSpotType(spot.getSpotType());
	        newSpot.setSpotInfo(spot.getSpotInfo());
	        newSpot.setSpotPic(spot.getSpotPic());
	        System.out.println("update succeeded");
	        return newSpot;
	    }
	    System.out.println("update failed");
	    return null;
	}
	

	public void deleteById(Integer spotNo) {
		sRepo.deleteById(spotNo);
	}

}
