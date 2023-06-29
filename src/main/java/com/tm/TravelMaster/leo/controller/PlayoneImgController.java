package com.tm.TravelMaster.leo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tm.TravelMaster.leo.model.Playone;
import com.tm.TravelMaster.leo.model.PlayoneImg;
import com.tm.TravelMaster.leo.service.PlayoneImgService;
import com.tm.TravelMaster.leo.service.PlayoneService;

@Controller
public class PlayoneImgController {

	@Autowired
	private PlayoneService pService;
	
	@Autowired
	private PlayoneImgService piService;
	
	@PostMapping("/playone/imgedit")
	public String playoneImgEdit(@RequestParam("updateimg") Integer playoneId, Model m) {
		List<PlayoneImg> playoneImgList =piService.findImgById(playoneId);
		 m.addAttribute("playoneImgList", playoneImgList);
		return "leo/background/updatePlayoneImg";
	}
	
//----------------------------------------新增功能------------------------------------------	

	@PostMapping("/playone/imgnew")
	public String handlePostRequest(@RequestParam("playoneId") String playoneId,
	                                @RequestParam("newPlayoneImg") MultipartFile newPlayoneImg,
	                                Model m) throws IOException {

	    byte[] imgbyte = newPlayoneImg.getBytes();

	    //如果回傳空值就不新增
	    if (imgbyte != null && imgbyte.length > 0) {
	        Playone playone = pService.findById(Integer.parseInt(playoneId));
	        if(playone != null) {
	            piService.insertPlayonePhoto(imgbyte, playone);
	        }
	    }

	    Playone playone = pService.findById(Integer.parseInt(playoneId));
	    m.addAttribute("playone", playone);

	    return "redirect:/playone/findid?id=" + playoneId;

	}



//----------------------------------------刪除功能------------------------------------------	

	@PostMapping("/playone/imgdelete")
    public ResponseEntity<String> deleteImage(@RequestParam("playoneimgid") Integer playoneImgId) {
        try {
        	piService.deleteById(playoneImgId);
            return ResponseEntity.ok("success") ;
        } catch(NumberFormatException e) {
            System.out.println("Invalid format for playoneimgid.");
            e.printStackTrace();
            return  ResponseEntity.ok("error") ;
        } catch(Exception e) {
            System.out.println("Error in deletion process.");
            e.printStackTrace();
            return ResponseEntity.ok("error") ;
        }
    }
	


//----------------------------------------其他功能------------------------------------------	
	
}
