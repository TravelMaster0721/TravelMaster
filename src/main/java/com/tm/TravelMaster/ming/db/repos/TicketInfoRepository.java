package com.tm.TravelMaster.ming.db.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.TravelMaster.ming.model.entity.TicketInfo;

@Repository
public interface TicketInfoRepository extends JpaRepository<TicketInfo, Integer>{

//	public TicketInfo getTicketInfoById(int ticketID);
//	public List<TicketInfo> getAllTicketInfo();
//	public boolean insertTicketInfo(TicketInfo ticket);
//	public boolean insertTicketInfo(Session session, TicketInfo ticket);
//	public boolean updateTicketInfo(TicketInfo ticket);
//	public boolean deleteTicketInfo(int ticketID);
	
}
