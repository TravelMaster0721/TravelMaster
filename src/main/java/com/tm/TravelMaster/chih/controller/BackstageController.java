package com.tm.TravelMaster.chih.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;

@Controller
@SessionAttributes("mbsession")
public class BackstageController {
	
	@Autowired
	private MemberService mService;
	
	@GetMapping("/tobackstage.controller")
	public String redirectToBackstage() {
		return "layout/backstage";
	}
	
	@GetMapping("/tobackstageallmember.controller")
	public String redirectToBackstageMember(Model m) {
		Member mb = (Member) m.getAttribute("mbsession");
		if(mb!=null) {
			 List<Member> memberList = mService.findAllOtherMember(mb);
	         m.addAttribute("memberList", memberList); 
	         return "chih/backstageAllMember";
		}
		else {
			return "chih/loginSystem";
		}
		
	}
	
	@GetMapping("/tobackstagepersonal.controller")
	public String redirectToBackstagePersonal(Model m) {
		Member mb = (Member) m.getAttribute("mbsession");
		if(mb!=null) {
			  Member member = mService.findByMemberNum(mb.getMemberNum());
	             m.addAttribute("member", member);  
			return "chih/personalBackstage";
		}
		else {
			return "chih/loginSystem";
		}
		
	}
	
}
