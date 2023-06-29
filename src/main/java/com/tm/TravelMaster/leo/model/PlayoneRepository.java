package com.tm.TravelMaster.leo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayoneRepository extends JpaRepository<Playone, Integer> {

	public Playone findByPlayoneNick(String playoneNick);

	public List<Playone> findByPlayoneNameAndFixedValue(String playoneName, Integer fixedValue);
	
	public Playone findByPlayoneIdAndFixedValue(Integer playoneId, Integer fixedValue);
	
	List<Playone> findByFixedValue(Integer fixedValue);
	
	@Query(value = "SELECT TOP 8 * FROM Playone WHERE fixedValue = 1 ORDER BY NEWID()", nativeQuery = true)
	List<Playone> findRandomEightByFixedValue();

}
