package com.tm.TravelMaster.yu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tm.TravelMaster.yu.model.SpotPic;
import com.tm.TravelMaster.yu.service.SpotPicService;

@Controller
public class SpotPicController {
	
	@Autowired
	private SpotPicService spService;
	
	@GetMapping("/photo/new")
	public String newPhoto() {
		return "photo/uploadPage";
	}
	
	
	@PostMapping("/photo/upload")
	public String uploadPhotoAction(
			@RequestParam("photoName") String name, 
			@RequestParam("photoFile") MultipartFile file) {
	
		try {
			SpotPic sp = new SpotPic();
			sp.setPhotoName(name);
			sp.setPhotoFile(file.getBytes());
			
			spService.insertPic(sp);
			
			return "redirect:/";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/photo/new";
		}
	}
	
	@GetMapping("/photo/listAllPhoto")
	public String getAllPhoto(Model model) {
		List<SpotPic> list = spService.listPic();
		model.addAttribute("photoList", list);
		return "photo/listPhoto";
	}
	
	@GetMapping("/downloadImage/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer id){
		SpotPic sp = spService.getPhotoById(id);
		byte[] photoFile = sp.getPhotoFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}
	
	@GetMapping("/photo/ajaxUpload")
	public String ajaxUpload() {
		return "photo/ajaxUploadPage";
	}
	
	@ResponseBody
	@PostMapping("/photo/ajaxPost")
	public String ajaxPost(@RequestParam("photoName") String photoName, @RequestParam("file") MultipartFile file) {
		SpotPic sp = new SpotPic();
		
		sp.setPhotoName(photoName);
		
		try {
			sp.setPhotoFile(file.getBytes());
			spService.insertPic(sp);
			
			return "update succeeded";
		} catch (IOException e) {
			e.printStackTrace();
			return "update failed";
		}
	}

}
