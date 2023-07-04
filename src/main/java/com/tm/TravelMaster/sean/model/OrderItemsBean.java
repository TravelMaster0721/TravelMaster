package com.tm.TravelMaster.sean.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orderItems")
@Data
public class OrderItemsBean {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "orderId")
	private OrdersBean orders;

	@Column(name = "productId")
	private Integer productId;
	
	@Column(name = "playoneId")
	private int playoneId;
	
	@Column(name = "productName")
	private String productName;
	
	@Column(name = "playoneNick")
	private String playoneNick;

	@Column(name = "productPrice")
	private Integer productPrice;
	
	@Column(name = "playonePrice")
	private Integer playonePrice;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "subTotal")
	private Integer subTotal;

}