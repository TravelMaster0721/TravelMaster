package com.tm.TravelMaster.chih.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	public Member findByMemberAccAndMemberPwd(String memberAcc, String memberPwd);
	
	
	@Query("FROM Member WHERE memberAcc = :memberAcc")
	public Member findByMemberAcc(@Param("memberAcc") String memberAcc);
	
	@Query("FROM Member WHERE memberNum=:memberNum")
	public Member findByMemberNum(@Param("memberNum") String memberNum);
	
	@Query("FROM Member WHERE memberNum!=:memberNum")
	public List<Member> findAllOtherMember(@Param("memberNum") String memberNum);
	
	@Modifying
	@Transactional
    @Query("UPDATE Member SET memberName = :memberName, memberMail = :memberMail, memberPhone = :memberPhone, memberAdd = :memberAdd, memberPwd = :memberPwd, memberLevel = :memberLevel WHERE memberNum = :memberNum")
	public void updateMemberNum(@Param("memberNum") String memberNum, @Param("memberName") String memberName, @Param("memberMail") String memberMail, @Param("memberPhone") String memberPhone, @Param("memberAdd") String memberAdd, @Param("memberPwd") String memberPwd, @Param("memberLevel") String memberLevel);
	
	
}
