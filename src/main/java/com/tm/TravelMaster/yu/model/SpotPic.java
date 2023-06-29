package com.tm.TravelMaster.yu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="spotPic")
public class SpotPic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	@Column(name="photoName")
	private String photoName;
	
	@Lob
	@Column(name="photoFile")
	private byte[] photoFile;
	
	public SpotPic() {
	}

	
}
