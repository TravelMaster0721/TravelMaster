package com.tm.TravelMaster.leo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.TravelMaster.leo.model.Playone;
import com.tm.TravelMaster.leo.model.PlayoneImg;
import com.tm.TravelMaster.leo.model.PlayoneImgRepository;
import com.tm.TravelMaster.leo.model.PlayoneRepository;

import jakarta.transaction.Transactional;

@Service
public class PlayoneService {
	
	@Autowired
	private PlayoneRepository pRepo;
	
	@Autowired
	private PlayoneImgRepository piRepo;

//----------------------------------------新增功能------------------------------------------	

	public Playone insertPlayone(Playone po) {
		return pRepo.save(po);
	}

//----------------------------------------修改功能------------------------------------------	

	@Transactional
	public Playone updatePlayoneById( String playoneId, String pNick, String pName, String pSex, String pAge, String pInterest, String pIntroduce) {
		Optional<Playone> optional = pRepo.findById(Integer.parseInt(playoneId));
		if (optional.isPresent()) {
			Playone playone = optional.get();
			playone.setPlayoneNick(pNick);
			playone.setPlayoneName(pName);
			playone.setPlayoneSex(pSex);
			playone.setPlayoneAge(Integer.parseInt(pAge));
			playone.setPlayoneInterest(pInterest);
			playone.setPlayoneIntroduce(pIntroduce);
			playone.setFixedValue(1);
			
			
			pRepo.save(playone);
			return playone;
		}
		System.out.println("No Playone with id: " + playoneId);
		return null;
	}

	
//----------------------------------------刪除功能------------------------------------------	
	
	@Transactional
	public Playone deleteById(Integer id) {
		Optional<Playone> optional = pRepo.findById(id);
		if (optional.isPresent()) {
			Playone p = optional.get();
			p.setFixedValue(0);
			
			for (PlayoneImg img : p.getPlayoneImgs()) {
				img.setFixedValue(0);
				piRepo.save(img);
			}
			
			pRepo.save(p);
			return p;
		}
		System.out.println("No Playone with id: " + id);
		return null;
	}
	
//----------------------------------------搜尋功能------------------------------------------	
	
	public List<Playone> findAll() {
		return pRepo.findByFixedValue(1);
	}
	
	public List<Playone> findRandomEightByFixedValue() {
	    return pRepo.findRandomEightByFixedValue();
	}

	
	public List<PlayoneImg> getPhotosByPlayoneId(Integer playoneId) {
	    return piRepo.findByPlayone_PlayoneId(playoneId);
	}
	
	public PlayoneImg getPhotoById(Integer id) {
		Optional<PlayoneImg> optional = piRepo.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Playone findById(Integer playoneId) {
	    Playone playone = pRepo.findByPlayoneIdAndFixedValue(playoneId, 1);
	    if (playone != null) {
	        List<PlayoneImg> images = piRepo.findByPlayone_PlayoneIdAndFixedValue(playoneId, 1);
	        playone.setPlayoneImgs(images);
	        return playone;
	    }
	    return null;
	}

	
	public List<Playone> findByName(String playoneName) {
	    List<Playone> playones = pRepo.findByPlayoneNameAndFixedValue(playoneName,1);
	    for (Playone playone : playones) {
	    	List<PlayoneImg> images = piRepo.findByPlayone_PlayoneIdAndFixedValue(playone.getPlayoneId(), 1);
	        playone.setPlayoneImgs(images);
	    }
	    return playones;
	}



//----------------------------------------其他功能------------------------------------------	
	
	public boolean checkIfplayoneNickExist(String playoneNick) {
		Playone dbUser = pRepo.findByPlayoneNick(playoneNick);
		if (dbUser!=null) {
			return true;
		}else {
			return false;
		}
	}
	
}
