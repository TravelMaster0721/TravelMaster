package com.tm.TravelMaster.chih.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.chih.model.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository mRepo;

	public void insertMember(Member mb) {
		mRepo.save(mb);
	}

	public boolean checkLogin(String memberAcc, String memberPwd) {
		Member member = mRepo.findByMemberAccAndMemberPwd(memberAcc, memberPwd);
		if (member != null) {
			return true;
		}
		return false;
	}

	public boolean findByMemberAcc(String memberAcc) {
		Member member = mRepo.findByMemberAcc(memberAcc);
		if (member != null) {
			return true;
		}
		return false;
	}

	public Member returnByMemberAcc(Member mb) {
		Member member = mRepo.findByMemberAcc(mb.getMemberAcc());
		return member;
	}

	public Member findByMemberNum(String memberNum) {
		Member member = mRepo.findByMemberNum(memberNum);
		return member;
	}
	
	public List<Member> findAllOtherMember(Member mb){
		List<Member> memberList = mRepo.findAllOtherMember(mb.getMemberNum());
		return memberList;
	}

	public List<Member> findAllMember() {
		List<Member> memberList = mRepo.findAll();
		return memberList;
	}

	public void updateMember(Member mb) {
		mRepo.updateMemberNum(mb.getMemberNum(), mb.getMemberName(), mb.getMemberMail(), mb.getMemberPhone(),
				mb.getMemberAdd(), mb.getMemberPwd(), mb.getMemberLevel());
	}
	
	
	public void deleteMember(Member mb) {
		mRepo.delete(mb);
	}
}
