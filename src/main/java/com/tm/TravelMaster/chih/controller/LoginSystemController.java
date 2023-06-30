package com.tm.TravelMaster.chih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginSystemController {

	@GetMapping("/login.controller")
	public String processMainAction(HttpServletRequest request, HttpSession session) {
	    String referrer = request.getHeader("Referer");

	    String loginPageUrl = "http://localhost:8080/TM/login.controller";
	    if (referrer != null && !referrer.contains(loginPageUrl)) {
	        session.setAttribute("url_prior_login", referrer);
	    }

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
	        String redirectTo = (String) session.getAttribute("url_prior_login");
	        if (redirectTo != null) {
	            session.removeAttribute("url_prior_login");
	            return "redirect:" + redirectTo;
	        } else {
	            return "redirect:/layout/index";
	        }
	    } else {
	        String errorMessage = "帳號密碼錯誤，請重新登入";
	        m.addAttribute("errorMessage", errorMessage);
	        return "chih/loginSystem";
	    }
	}

	
	@GetMapping("/logout.controller")
	public String StringprocessAction(HttpSession session, HttpServletRequest request) {
	    session.invalidate();

	    String referrer = request.getHeader("Referer");
	    if (referrer != null) {
	        return "redirect:" + referrer;
	    } else {
	        return "redirect:/layout/index";
	    }
	}

}
