package com.tm.TravelMaster.leo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayoneImgRepository extends JpaRepository<PlayoneImg, Integer> {
	List<PlayoneImg> findByPlayone_PlayoneId(Integer playoneId);
	List<PlayoneImg> findByPlayone_PlayoneIdAndFixedValue(Integer playoneId, Integer fixedValue);
	Optional<PlayoneImg> findByPlayoneimgId(Integer playoneimgId);
	
}
