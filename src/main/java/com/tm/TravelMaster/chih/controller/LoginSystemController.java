package com.tm.TravelMaster.chih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginSystemController {

	@GetMapping("/login.controller")
	public String processMainAction() {
		return "chih/loginSystem";
	}

	@Autowired
	private MemberService mService;

	@PostMapping("/checklogin.controller")
	public String processAction(@RequestParam("memberacc") String memberAcc,
			@RequestParam("memberpwd") String memberPwd,HttpSession session, Model m) {
		boolean result = mService.checkLogin(memberAcc,memberPwd);
		if (result) {
			Member mb = mService.returnByMemberAcc(new Member(memberAcc));
			session.setAttribute("mbsession", mb);
			return "redirect:/layout/index";
		} else {
			String errorMessage = "帳號密碼錯誤，請重新登入";
		    m.addAttribute("errorMessage", errorMessage);
		    return "chih/loginSystem";
		}
	}
	
	@GetMapping("/logout.controller")
	public String StringprocessAction(HttpSession session) {
		  session.invalidate();
		  return "redirect:/layout/index";
	}
}
