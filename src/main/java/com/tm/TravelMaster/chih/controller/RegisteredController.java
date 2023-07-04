package com.tm.TravelMaster.chih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;


@Controller
public class RegisteredController {

	@Autowired
	private MemberService mService;

	@GetMapping("/registered.controller")
	public String showRegistrationForm() {
		return "chih/registered";
	}

	@PostMapping("/registered.controller")
	public String registerMember(@RequestParam("membername") String memberName, @RequestParam("membersex") String memberSex,
			@RequestParam("membermail") String memberMail, @RequestParam("memberphone") String memberPhone,
			@RequestParam("memberadd") String memberAdd, @RequestParam("memberid") String memberId,
			@RequestParam("memberacc") String memberAcc, @RequestParam("memberpwd") String memberPwd, Model m) {
		
			Member mb = new Member();
			mb.setMemberName(memberName);
			mb.setMemberSex(memberSex);
			mb.setMemberMail(memberMail);;
			mb.setMemberPhone(memberPhone);;
			mb.setMemberAdd(memberAdd);;
			mb.setMemberId(memberId.toUpperCase());;
			mb.setMemberAcc(memberAcc);;
			mb.setMemberPwd(memberPwd);;
			mService.insertMember(mb);
			m.addAttribute("registerSuccess", "註冊成功，請重新登入!");
			return "chih/loginSystem";
		
	}
	
	@GetMapping("/CheckMemberAccAjax.controller")
    @ResponseBody
    public String checkMemberAcc(@RequestParam("memberacc") String memberacc) {
        boolean result = mService.findByMemberAcc(memberacc);
        if (result) {
            return "exists";
        } else {
            return "not_exists";
        }
    }

}
