package com.tm.TravelMaster.ming.controller;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tm.TravelMaster.ming.db.service.HighSpeedRailService;
import com.tm.TravelMaster.ming.db.service.TicketInfoService;
import com.tm.TravelMaster.ming.model.dto.BookingGoForm;
import com.tm.TravelMaster.ming.model.dto.HighSpeedRailTicket;
import com.tm.TravelMaster.ming.model.dto.TrainTimeInfo;
import com.tm.TravelMaster.ming.model.entity.TicketInfo;
import com.tm.TravelMaster.ming.model.entity.TranInfo;
import com.tm.TravelMaster.ming.model.entity.TicketInfoGroup;

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
		boolean uploadResult = true;
		String resultErrMsg = "";

//		另一種方法 這樣loop滾一次就夠了
//		try {
//			uploadResult = highSpeedRailService.insertTranInfoByCSV(fileContent);
//		} catch (SQLException e) {
//			uploadResult = false;
//			resultErrMsg = e.getMessage();
//		}
//		String json = String.format("{\"result\":%s, \"msg\":\"%s\"}", uploadResult ? "true" : "false",
//				uploadResult ? "批次新增成功" : String.format("批次新增失敗(%s)", resultErrMsg));
//		System.out.println(json);
//		return json;

		String[] fileContentList = fileContent.split(System.lineSeparator());
		String TranNo, StationID, TrainArrivalTime;
		TranInfo tInfo;
		List<TranInfo> tInfos = new ArrayList<>();
		for (String fileContentRow : fileContentList) {
			String[] datas = fileContentRow.split(",");
			// 驗證檔案格式
			if (datas.length != 3) {
				uploadResult = false;
				resultErrMsg = "檔案格式錯誤";
				break;
			}
			TranNo = datas[0].trim();
			StationID = datas[1].trim();
			TrainArrivalTime = datas[2].trim();
			// 驗證資料格式(excel轉成csv需先以記事本另存檔案轉UTF-8，避免產生髒資料)
			if (!TranNo.matches("[0-9]+")) {// -> 至少一個數字
				uploadResult = false;
				resultErrMsg = "TranNo格式錯誤";
				break;
			}
			if (!StationID.matches("[0-9]+")) { // -> 至少一個數字
				uploadResult = false;
				resultErrMsg = "StationID格式錯誤";
				break;
			}
			if (!TrainArrivalTime.matches("[0-9]{1,2}:[0-9]{1,2}")) { // -> 兩個數字 + ":" + 兩個數字
				uploadResult = false;
				resultErrMsg = "TrainArrivalTime格式錯誤";
				break;
			}

			// 0: TranNo
			// 1: StationID
			// 2: TrainArrivalTime
			tInfo = new TranInfo();
			tInfo.setTranNo(TranNo);
			tInfo.setStationID(StationID);
			tInfo.setTrainArrvialTime(TrainArrivalTime);
			tInfos.add(tInfo);
		}
		// 所有資料都要對 才可以開始 insert table, 但是這樣會多滾一次loop, 所以可以用另一種寫法 XD 給你參考
		if (uploadResult) {
			try {
				highSpeedRailService.insertTranInfos(tInfos);
			} catch (SQLException e) {
				uploadResult = false;
				resultErrMsg = e.getMessage();
			}
		}

		String json = String.format("{\"result\":%s, \"msg\":\"%s\"}", uploadResult ? "true" : "false",
				uploadResult ? "批次新增成功" : String.format("批次新增失敗(%s)", resultErrMsg));
		System.out.println(json);
		return json;
	}

	@PostMapping(value = "/bookingGo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String bookingGo(@RequestBody BookingGoForm bookingGoForm, Model model) {
		Date today = new Date();
		String resultErrMsg = "";
		boolean result = true;
		if (bookingGoForm.getFormInputVal_selectedSeats() == null
				|| bookingGoForm.getFormInputVal_selectedSeats().isEmpty()) {
			result = false;
			resultErrMsg = "前端資料獲取失敗";
		}
		TicketInfoGroup shoppingCart = null;
		if (result) {
			String[] selectedSeats = bookingGoForm.getFormInputVal_selectedSeats().split(",");
			List<TicketInfo> ticketInfos = new ArrayList<>();
			TicketInfo ticketInfo;

			for (String selectedSeat : selectedSeats) {
				ticketInfo = new TicketInfo();
				ticketInfo.setTranNo(bookingGoForm.getFormInputVal_TranNo());
				ticketInfo.setSeat(selectedSeat);
				ticketInfo.setDepartureST(bookingGoForm.getFormInputVal_DepartureStation());
				ticketInfo.setDestinationST(bookingGoForm.getFormInputVal_ArrivalStation());
				ticketInfo.setDeparturedate(bookingGoForm.getFormInputVal_DepartureDate());
				ticketInfo.setDeparturetime(bookingGoForm.getFormInputVal_DepartureTime());
				ticketInfo.setArrivaltime(bookingGoForm.getFormInputVal_ArrivalTime());
				ticketInfo.setPrice(Integer.parseInt(bookingGoForm.getFormInputVal_price()));
				ticketInfo.setBookingdate(today);
				ticketInfos.add(ticketInfo);
			}
			shoppingCart = new TicketInfoGroup();
			shoppingCart.setTicketInfos(ticketInfos);
			shoppingCart.setMember_id(bookingGoForm.getFormInputVal_memberId());
			shoppingCart.setStatus(0);
			try {
				 // 這裡的目的要是拿到剛剛insert的Cart_Id是多少， 因為你的ID 是讓DB自己去長的
				shoppingCart = ticketsService.insertShoppingCart(shoppingCart);
			} catch (SQLException e) {
				resultErrMsg = e.getMessage();
			}
		}
		String json = String.format("{\"result\":%s, \"msg\":\"%s\", \"ticketCartId\":\"%s\"}",
				result ? "true" : "false", 
				result ? "資料儲存成功" : String.format("資料儲存失敗(%s)", resultErrMsg),
				shoppingCart == null ? "-1" : shoppingCart.getCart_Id()); // 把Cart_Id 丟回AJAX
		System.out.println(json);
		return json;
	}

}
