package com.tm.TravelMaster.sean.model;

import java.util.Date;
import java.util.Set;

import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.leo.model.Playone;
import com.tm.TravelMaster.ming.model.TicketInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.HashSet;

@Entity
@Table(name = "orderRecord")
@Data
public class OrderRecordBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	@ManyToOne
	@JoinColumn(name = "memberSeq", referencedColumnName = "memberSeq")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "productId", referencedColumnName = "productId")
	private ProductBean product;

	@ManyToOne
	@JoinColumn(name = "playoneId", referencedColumnName = "playoneId")
	private Playone playone;

	@ManyToOne
	@JoinColumn(name = "TicketID", referencedColumnName = "TicketID")
	private TicketInfo ticketInfo;

	@Column(name = "totalPrice")
	private Integer totalPrice;

	@Column(name = "paymentTime")
	private Date paymentTime;

	@Column(name = "paymentStatus")
	private Integer paymentStatus;

	@OneToMany(mappedBy = "orderRecord", cascade = CascadeType.ALL)
	private Set<OrderDetailsBean> orderDetails = new HashSet<>();

}
