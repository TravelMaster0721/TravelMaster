package com.tm.TravelMaster.chih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;



@Controller
@SessionAttributes("mbsession")
public class UpdateDeleteMemberController {
	
	@Autowired
	private MemberService mService;
	
	@PutMapping("/updateallmember.controller")
	public String updateAllMember(@RequestParam("check") String check,@RequestParam("membernum") String membernum,@RequestParam("membername") String mambername,
			@RequestParam("membermail") String membermail,@RequestParam("memberphone") String memberphone,@RequestParam("memberadd") String memberadd,@RequestParam("memberpwd") String memberpwd,@RequestParam("memberlevel") String memberlevel,Model m) {
		if(check.equals("Y")) {			
			Member mb = mService.findByMemberNum(membernum);
			mb.setMemberName(mambername);
			mb.setMemberMail(membermail);
			mb.setMemberPhone(memberphone);
			mb.setMemberAdd(memberadd);
			mb.setMemberPwd(memberpwd);
			mb.setMemberLevel(memberlevel);
			mService.updateMember(mb);
		}
		return "redirect:/tobackstageallmember.controller";
	}
	
	@DeleteMapping("/deletemember.controller")
	public String deleteAllMember(@RequestParam("check") String check,@RequestParam("numdelete") String membernum,Model m) {
		if(check.equals("Y")) {	
			Member mb = mService.findByMemberNum(membernum);
			mService.deleteMember(mb);
		}
		return "redirect:/tobackstageallmember.controller";
	}
	
	@PutMapping("/updatemember.controller")
	public String updateMember(@RequestParam("check") String check,@RequestParam("membernum") String membernum,@RequestParam("membername") String mambername,
			@RequestParam("membermail") String membermail,@RequestParam("memberphone") String memberphone,@RequestParam("memberadd") String memberadd,@RequestParam("memberpwd") String memberpwd,Model m) {
		if(check.equals("Y")) {	
			Member mb = mService.findByMemberNum(membernum);
			mb.setMemberName(mambername);
			mb.setMemberMail(membermail);
			mb.setMemberPhone(memberphone);
			mb.setMemberAdd(memberadd);
			mb.setMemberPwd(memberpwd);
			mService.updateMember(mb);
		}
		Member member = mService.findByMemberNum(membernum);
		m.addAttribute("member", member);
		return "chih/personalBackstage";
	}
	
	@PutMapping("/updatepersonalmember.controller")
	public String updatePersonalMember(@RequestParam("check") String check,@RequestParam("membernum") String membernum,@RequestParam("membername") String mambername,
			@RequestParam("membermail") String membermail,@RequestParam("memberphone") String memberphone,@RequestParam("memberadd") String memberadd,@RequestParam("memberpwd") String memberpwd,Model m) {
		if(check.equals("Y")) {	
			Member mb = mService.findByMemberNum(membernum);
			mb.setMemberName(mambername);
			mb.setMemberMail(membermail);
			mb.setMemberPhone(memberphone);
			mb.setMemberAdd(memberadd);
			mb.setMemberPwd(memberpwd);
			mService.updateMember(mb);
		}
		Member mb = mService.findByMemberNum(membernum);
		m.addAttribute("mbsession", mb);
		return "redirect:/layout/index";
	}
	
}
