package com.tm.TravelMaster.sean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orderDetails")
@Data
public class OrderDetailsBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderDetailId;

	@ManyToOne
	@JoinColumn(name = "orderId", referencedColumnName = "orderId")
	private OrderRecordBean orderRecord;

	@Column(name = "totalPrice")
	private Integer totalPrice;

}
