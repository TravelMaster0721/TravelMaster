package com.tm.TravelMaster.chih.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
// 沒分頁版本	
//	@GetMapping("/tobackstageallmember.controller")
//	public String redirectToBackstageMember(Model m) {
//		Member mb = (Member) m.getAttribute("mbsession");
//		if(mb!=null) {
//			 List<Member> memberList = mService.findAllOtherMember(mb);
//	         m.addAttribute("memberList", memberList); 
//	         m.addAttribute("mbsession", mb);
//	         return "chih/backstageAllMember";
//		}
//		else {
//			return "chih/loginSystem";
//		}
//		
//	}

	@GetMapping("/tobackstagepersonal.controller")
	public String redirectToBackstagePersonal(Model m) {
		Member mb = (Member) m.getAttribute("mbsession");
		if (mb != null) {
			Member member = mService.findByMemberNum(mb.getMemberNum());
			m.addAttribute("member", member);
			return "chih/personalBackstage";
		} else {
			return "chih/loginSystem";
		}

	}

	@ResponseBody
	@GetMapping("/findnewallmember.controller")
	public List<Member> findToBackstageAllMember(@RequestParam String mytext,@RequestParam String text,Model m) {
		Member mb = (Member) m.getAttribute("mbsession");
		if(mb!=null) {
			 List<Member> memberList = mService.findByMemberNumOrMemberLevel(mytext,text);
	         m.addAttribute("memberList", memberList); 
	         return memberList;
		}
		else {
			return null;
		}
		
	}

	@GetMapping("/tobackstageallmember.controller")
	public String pageMember(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model m) {
		Member mb = (Member) m.getAttribute("mbsession");
		if (mb != null) {
			Page<Member> page = mService.findByPage(pageNumber, mb);
			m.addAttribute("memberList", page);
			return "chih/backstageAllMember";
		} else {
			return "chih/loginSystem";
		}
	}

}
