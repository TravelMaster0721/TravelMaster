package com.tm.TravelMaster.yu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpotRepository extends JpaRepository<Spot, Integer> {
	
	@Query("SELECT DISTINCT s.cityName FROM Spot s")
    List<String> findAllCityNames();
	
	@Query("SELECT DISTINCT s.spotType FROM Spot s")
    List<String> findAllSpotTypes();
}
