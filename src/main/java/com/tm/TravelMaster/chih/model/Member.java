package com.tm.TravelMaster.chih.model;

import org.springframework.stereotype.Component;

import com.tm.TravelMaster.leo.model.Playone;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
@Component
public class Member {

	@Id
	@Column(name = "memberSeq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int memberSeq;

	@Column(name = "memberNum", insertable = false)
	private String memberNum;

	@Column(name = "memberName")
	private String memberName;

	@Column(name = "memberSex")
	private String memberSex;

	@Column(name = "memberMail")
	private String memberMail;

	@Column(name = "memberPhone")
	private String memberPhone;

	@Column(name = "memberAdd")
	private String memberAdd;

	@Column(name = "memberId")
	private String memberId;

	@Column(name = "memberAcc")
	private String memberAcc;

	@Column(name = "memberPwd")
	private String memberPwd;

	@Column(name = "memberLevel", insertable = false)
	private String memberLevel;
	
	@Column(name = "memberPhoto")
	private byte[] memberPhoto;

	@Column(name = "resetPwdToken")
	private String resetPwdToken;
	
	@Column(name = "auth_provider")
	private String auth_provider;
	
	public String getAuth_provider() {
		return auth_provider;
	}

	public void setAuth_provider(String auth_provider) {
		this.auth_provider = auth_provider;
	}

	@Column(name = "playoneLevel",columnDefinition = "int default 0")
    private int playoneLevel;
	
	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private Playone playone;
	
	public Member() {
	}

	public Member(String memberAcc) {
		this.memberAcc = memberAcc;
	}

	public Member(String memberAcc, String memberPwd) {
		this.memberAcc = memberAcc;
		this.memberPwd = memberPwd;
	}

	public Member(String memberName, String memberSex, String memberMail, String memberPhone, String memberAdd,
			String memberId, String memberAcc, String memberPwd) {
		this.memberName = memberName;
		this.memberSex = memberSex;
		this.memberMail = memberMail;
		this.memberPhone = memberPhone;
		this.memberAdd = memberAdd;
		this.memberId = memberId;
		this.memberAcc = memberAcc;
		this.memberPwd = memberPwd;
	}

	public Member(String memberNum, String memberName, String memberSex, String memberMail, String memberPhone,
			String memberAdd, String memberId, String memberAcc, String memberPwd, String memberLevel) {
		this.memberNum = memberNum;
		this.memberName = memberName;
		this.memberSex = memberSex;
		this.memberMail = memberMail;
		this.memberPhone = memberPhone;
		this.memberAdd = memberAdd;
		this.memberId = memberId;
		this.memberAcc = memberAcc;
		this.memberPwd = memberPwd;
		this.memberLevel = memberLevel;
	}

	public int getMemberSeq() {
		return memberSeq;
	}

	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
	}

	public String getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}

	public String getMemberMail() {
		return memberMail;
	}

	public void setMemberMail(String memberMail) {
		this.memberMail = memberMail;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberAdd() {
		return memberAdd;
	}

	public void setMemberAdd(String memberAdd) {
		this.memberAdd = memberAdd;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberAcc() {
		return memberAcc;
	}

	public void setMemberAcc(String memberAcc) {
		this.memberAcc = memberAcc;
	}

	public String getMemberPwd() {
		return memberPwd;
	}

	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public byte[] getMemberPhoto() {
		return memberPhoto;
	}

	public void setMemberPhoto(byte[] memberPhoto) {
		this.memberPhoto = memberPhoto;
	}

	public String getResetPwdToken() {
		return resetPwdToken;
	}

	public void setResetPwdToken(String resetPwdToken) {
		this.resetPwdToken = resetPwdToken;
	}

	public int getPlayoneLevel() {
		return playoneLevel;
	}

	public void setPlayoneLevel(int playoneLevel) {
		this.playoneLevel = playoneLevel;
	}

	public Playone getPlayone() {
		return playone;
	}

	public void setPlayone(Playone playone) {
		this.playone = playone;
	}

	
	

	
}
