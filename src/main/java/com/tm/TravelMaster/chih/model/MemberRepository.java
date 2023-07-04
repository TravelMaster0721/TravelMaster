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
	
	@Query("FROM Member WHERE memberNum != :myMemberNum AND (memberNum LIKE %:memberNum% OR memberLevel LIKE %:memberLevel%)")
	public List<Member> findByMemberNumOrMemberLevel(@Param("myMemberNum") String myMemberNum,@Param("memberNum") String memberNum, @Param("memberLevel") String memberLevel);
	
	@Modifying
	@Transactional
    @Query("UPDATE Member SET memberPhoto = :memberPhoto WHERE memberNum = :memberNum")
	public void updateMemberPhoto(@Param("memberNum") String memberNum,@Param("memberPhoto") byte[] memberPhoto);
	
	@Query("From Member WHERE resetPwdToken = :resetPwdToken")
	public Member findByResetPwdToken(@Param("resetPwdToken") String resetPwdToken);
	
	@Query("From Member WHERE memberMail = :memberMail")
	public Member findByMemberMail(@Param("memberMail") String memberMail);
	
	@Modifying
	@Transactional
    @Query("UPDATE Member SET memberPwd = :memberPwd WHERE memberNum = :memberNum")
	public void updateMemberPwd(@Param("memberNum") String memberNum,@Param("memberPwd") String memberPwd);
	
}
