package com.tm.TravelMaster.ming.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.TravelMaster.ming.db.repos.PriceInfoRepository;
import com.tm.TravelMaster.ming.db.repos.StationInfoRepository;
import com.tm.TravelMaster.ming.db.repos.TrainInfoRepository;
import com.tm.TravelMaster.ming.db.repos.TrainTimeInfoRepostory;
import com.tm.TravelMaster.ming.model.PriceInfo;
import com.tm.TravelMaster.ming.model.StationInfo;
import com.tm.TravelMaster.ming.model.TicketInfo;
import com.tm.TravelMaster.ming.model.TranInfo;
import com.tm.TravelMaster.ming.model.dto.HighSpeedRailTicket;
import com.tm.TravelMaster.ming.model.dto.TrainTimeInfo;

@Service
public class HighSpeedRailService {

	Logger logger = LoggerFactory.getLogger(HighSpeedRailService.class);

	private Map<Integer, String> g_stationMap = null;

	private Map<Set<String>, Integer> g_priceInfoMap = null;

	@Autowired
	private TicketInfoService ticketService;

	@Autowired
	private StationInfoRepository stationInfoRepos;

	@Autowired
	private TrainInfoRepository tranInfoRepos;

	@Autowired
	private PriceInfoRepository priceInfoRepos;

	@Autowired
	private TrainTimeInfoRepostory trainTimeInfoRepostory;

	// 建立分頁
	public Page<TrainTimeInfo> findByPage(String departureST, String destinationST, String departureTime,
			Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 10);
		Page<TrainTimeInfo> page = trainTimeInfoRepostory.getTrainTimeInfo(departureST, destinationST, departureTime,
				pgb);
		return page;
	}

	// 查詢單筆訂票資訊
	public HighSpeedRailTicket getBookingTkById(int ticketID) {
		Map<Integer, String> stationMap = getStationInfoMap();

		TicketInfo ticketInfo = ticketService.findTicketInfoById(ticketID);
		HighSpeedRailTicket result = new HighSpeedRailTicket(ticketInfo);
		result.setDepartureST(stationMap.get(Integer.parseInt(ticketInfo.getDepartureST())));
		result.setDestinationST(stationMap.get(Integer.parseInt(ticketInfo.getDestinationST())));
		return result;
	}

	// 查詢所有訂票資訊
	public List<HighSpeedRailTicket> getAllBookingTk() {

		List<TicketInfo> ticketInfoLst = ticketService.findAllTicketInfo();

		Map<Integer, String> stationMap = getStationInfoMap();

		List<HighSpeedRailTicket> result = new ArrayList<>();
		for (TicketInfo ticketInfo : ticketInfoLst) {
			HighSpeedRailTicket bookingTk = new HighSpeedRailTicket(ticketInfo);
			bookingTk.setDepartureST(stationMap.get(Integer.parseInt(ticketInfo.getDepartureST())));
			bookingTk.setDestinationST(stationMap.get(Integer.parseInt(ticketInfo.getDestinationST())));
			result.add(bookingTk);
		}
		return result;

	}

	public Map<Set<String>, Integer> getPriceInfoMap() {
		if (g_priceInfoMap == null) {
			initPriceInfoMap();
		}
		return g_priceInfoMap;
	}

	private void initPriceInfoMap() {
		logger.info("======initPriceInfoMap");
		g_priceInfoMap = new HashMap<Set<String>, Integer>();
		List<PriceInfo> priceList = findAllPriceInfo();
		for (PriceInfo price : priceList) {
			Set<String> tmpS = new HashSet<String>();
			tmpS.add(price.getDepartureST());
			tmpS.add(price.getDestinationST());
			g_priceInfoMap.put(tmpS, price.getPrice());
		}
	}

	public Map<Integer, String> getStationInfoMap() {
		if (g_stationMap == null) {
			initStationInfoMap();
		}
		return g_stationMap;
	}

	private void initStationInfoMap() {
		logger.info("======initStationInfoMap");
		g_stationMap = new HashMap<Integer, String>();
		List<StationInfo> stationInfos = findAllStationInfo();
		for (StationInfo stationInfo : stationInfos) {
			g_stationMap.put(stationInfo.getStationID(), stationInfo.getStationName());
		}
	}

	public List<StationInfo> findAllStationInfo() {
		return stationInfoRepos.findAll();
	}

	public List<PriceInfo> findAllPriceInfo() {
		return priceInfoRepos.findAll();
	}

	public List<TranInfo> findAllTranInfo() {
		return tranInfoRepos.findAll();
	}

	// 新增時刻表
	@Transactional( rollbackFor = SQLException.class)
	public void insertTranInfo(TranInfo train) {
		tranInfoRepos.save(train);
	}

	// 編輯時刻表
	@Transactional
	public void updateByTranNoAndStationID(TranInfo tif) {

		tranInfoRepos.updateByTranNoAndStationID(tif.getTranNo(), Integer.toString(tif.getStationID()),
				tif.getTrainArrvialTime());
	}

	// 查詢時刻表
	public List<TrainTimeInfo> getAllTrainTimeInfo() {
		return trainTimeInfoRepostory.getTrainTimeInfo();
	}

	// 刪除時刻表
	@Transactional
	public boolean deleteTrainInfoByTranNo(String tranNo) {
		if (tranNo != null) {
			tranInfoRepos.deleteByTranNo(tranNo);
			System.out.println("delete data");
			return true;
		}
		return false;
	}

}
