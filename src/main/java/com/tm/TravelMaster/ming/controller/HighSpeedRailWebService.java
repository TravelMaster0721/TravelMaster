package com.tm.TravelMaster.ming.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tm.TravelMaster.ming.db.service.HighSpeedRailService;
import com.tm.TravelMaster.ming.db.service.TicketInfoService;
import com.tm.TravelMaster.ming.model.TranInfo;
import com.tm.TravelMaster.ming.model.dto.HighSpeedRailTicket;
import com.tm.TravelMaster.ming.model.dto.TrainTimeInfo;

@Controller
@RequestMapping("/services")
public class HighSpeedRailWebService {

	@Autowired
	private TicketInfoService ticketsService;

	@Autowired
	private HighSpeedRailService highSpeedRailService;

	@GetMapping(value = "/GetTranInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String GetTranInfo(@RequestParam("departureST") String departureST,
			@RequestParam("destinationST") String destinationST, @RequestParam("departureTime") String departureTime,
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model model) {

		Page<TrainTimeInfo> trainTimeInfos = highSpeedRailService.findByPage(departureST, destinationST, departureTime,
				pageNumber);

		String json = new Gson().toJson(trainTimeInfos);
		return json;
	}

	@GetMapping("/GetAllTicketInfo")
	@ResponseBody
	public String GetAllTicketInfo() {
		List<HighSpeedRailTicket> tks = highSpeedRailService.getAllBookingTk();
		Map<String, List<List<String>>> inputMap = new HashMap<String, List<List<String>>>();
		List<List<String>> dataList = new ArrayList<List<String>>();

		for (HighSpeedRailTicket tk : tks) {
			List<String> tkDataLst = new ArrayList<String>();
			tkDataLst.add(Integer.toString(tk.getTicketID()));
			tkDataLst.add(tk.getTranNo());
			tkDataLst.add(tk.getSeat());
			tkDataLst.add(tk.getDepartureST());
			tkDataLst.add(tk.getDestinationST());
			tkDataLst.add(tk.getDeparturedate());
			tkDataLst.add(tk.getDeparturetime());
			tkDataLst.add(tk.getArrivaltime());
			tkDataLst.add(Integer.toString(tk.getPrice()));
			tkDataLst.add(tk.getBookingdate());
			tkDataLst.add("<button class=\"btn btn-light\" onclick=\"updateTarget(" + tk.getTicketID()
					+ ")\"><i class=\"fa-solid fa-pen-to-square\"></i> </button>");
			tkDataLst.add("<button class=\"btn btn-light\" onclick=\"deleteTarget(" + tk.getTicketID()
					+ ")\"><i class=\"fa-solid fa-trash-can\"></i> </button>");
			dataList.add(tkDataLst);
		}
		inputMap.put("data", dataList);

		String json = new Gson().toJson(inputMap);
		return json;
	}

	@GetMapping(value = "/DeleteTicketInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String DeleteTicketInfo(@RequestParam("id") String id) {
		boolean isSucceed = ticketsService.deleteTickerInfoById(Integer.parseInt(id));
		String msg = Boolean.toString(isSucceed);
		String json = String.format("{\"id\":\"%s\", \"result\":%s}", id, msg);
		return json;
	}

	@PostMapping(value = "/BatchUploadTrainInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String BatchUploadTrainInfo(@RequestParam("file") MultipartFile file) throws IOException {
		String fileContent = new String(file.getBytes());
		String[] fileContentList = fileContent.split("\n");
		boolean uploadResult = true;
		TranInfo tInfo;
		// service.beginTransaction();
		for (String fileContentRow : fileContentList) {
			// if 1st row is column need to skip
			String[] datas = fileContentRow.split(",");
			/*
			 * if(datas format error){ uploadResult = false; break; }
			 */

			// 0: TranNo
			// 1: StationID
			// 2: TrainArrivalTime
			tInfo = new TranInfo();
			tInfo.setTranNo(datas[0]);
			tInfo.setStationID(Integer.parseInt(datas[1]));
			tInfo.setTrainArrvialTime(datas[2]);
			// service.insert(tInfo);
		}
		/**
		 * if(uploadResult){ 
		 *   service.commit(); 
		 * }else{ 
		 *   service.rollback(); 
		 * }
		 */

		String json = String.format("{\"result\":%s, \"msg\":%s}", 
				uploadResult ? "true" : "false",
				uploadResult ? "批次新增成功" : "批次新增失敗");
		return json;
	}
}
