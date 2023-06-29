package com.tm.TravelMaster.yu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tm.TravelMaster.yu.model.SpotPic;
import com.tm.TravelMaster.yu.model.SpotPicRepository;

@Service
public class SpotPicService {
	
	@Autowired
	private SpotPicRepository spRepo;
	
	public SpotPic insertPic(SpotPic sp) {
		return spRepo.save(sp);
	}
	
	public List<SpotPic> listPic(){
		return spRepo.findAll();
	}
	
	public SpotPic getPhotoById(Integer id) {
		Optional<SpotPic> optional = spRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
		
	}

}
