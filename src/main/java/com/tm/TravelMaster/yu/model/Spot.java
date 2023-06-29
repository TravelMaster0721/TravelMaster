package com.tm.TravelMaster.yu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="spot")
public class Spot {
	
	@Id
	@Column(name="spotNo")
	private Integer spotNo;
	
	@Column(name="spotName")
	private String spotName;
	
	@Column(name="cityName")
	private String cityName;

	@Column(name="spotPrice")
	private String spotPrice;

	@Column(name="spotType")
	private String spotType;
	
	@Column(name="spotInfo")
	private String spotInfo;
	
	@Column(name="spotPic")
	private String spotPic;
	
	public Spot() {
	}
	
}
