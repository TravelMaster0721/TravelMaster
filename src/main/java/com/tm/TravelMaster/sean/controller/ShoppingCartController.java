package com.tm.TravelMaster.sean.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.leo.model.Playone;
import com.tm.TravelMaster.ming.db.service.HighSpeedRailService;
import com.tm.TravelMaster.ming.db.service.TicketInfoService;
import com.tm.TravelMaster.ming.model.entity.ShoppingCart;
import com.tm.TravelMaster.ming.model.entity.TicketInfo;
import com.tm.TravelMaster.sean.model.LinePayRequest;
import com.tm.TravelMaster.sean.model.LinePayResponse;
import com.tm.TravelMaster.sean.model.LinePayStatusResponse;
import com.tm.TravelMaster.sean.model.OrdersBean;
import com.tm.TravelMaster.sean.model.ProductBean;
import com.tm.TravelMaster.sean.service.LinePayService;
import com.tm.TravelMaster.sean.service.ShoppingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShoppingCartController {

	@Autowired
	private TicketInfoService ticketsService;
	@Autowired
	private HighSpeedRailService highSpeedRailService;
	
	private final ShoppingService shoppingService;
	private final LinePayService linePayService;

	public ShoppingCartController(ShoppingService shoppingService, LinePayService linePayService) {
		this.shoppingService = shoppingService;
		this.linePayService = linePayService;
	}

	// 進購物車-登入判定+查詢所有
	@GetMapping("/sean/CartLoginStatus")
	public String handleCartLoginStatus(HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("mbsession");

		if (member != null) {
			String memberNum = member.getMemberNum();

			// 加載購物車資訊
			List<ProductBean> productCart = shoppingService.loadProductCartData(memberNum);
			List<Playone> playoneCart = shoppingService.loadPlayoneCartData(memberNum);

			// 將購物車資料添加到 Model 中
			model.addAttribute("products", productCart);
			model.addAttribute("playones", playoneCart);

			return "sean/ShoppingCart";
		} else {
			return "redirect:/login.controller";
		}
	}

	// 行程-加入購物車
	@PostMapping(value = "/sean/addToCart", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> addToCart(@ModelAttribute("product") ProductBean product, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");

		if (member == null) {
			return new ResponseEntity<>("請先登入", HttpStatus.UNAUTHORIZED);
		}

		String memberNum = member.getMemberNum();

		// 加載購物車資訊
		List<ProductBean> cart = shoppingService.loadProductCartData(memberNum);

		// 在 Service 層進行產品 ID 判定
		boolean isDuplicate = shoppingService.isProductInCart(cart, product.getProductId());

		if (isDuplicate) {
			return new ResponseEntity<>("商品已存在於購物車中", HttpStatus.OK);
		} else {
			// 添加產品到購物車
			cart.add(product);

			// 保存購物車資訊至本地端 JSON
			shoppingService.saveCartData(memberNum, cart);

			return new ResponseEntity<>("已成功加入商品", HttpStatus.OK);
		}
	}

	// 旅伴-加入購物車
	@PostMapping(value = "/sean/addPlayoneToCart", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> addPlayoneToCart(@ModelAttribute("playone") Playone playone, HttpSession session) {

		Member member = (Member) session.getAttribute("mbsession");

		if (member == null) {
			return new ResponseEntity<>("請先登入", HttpStatus.UNAUTHORIZED);
		}

		String memberNum = member.getMemberNum();

		// 加載購物車資訊
		List<Playone> cart = shoppingService.loadPlayoneCartData(memberNum);

		// 在 Service 層進行產品 ID 判定
		boolean isDuplicate = shoppingService.isPlayoneInCart(cart, playone.getPlayoneId());

		if (isDuplicate) {
			return new ResponseEntity<>("商品已存在於購物車中", HttpStatus.OK);
		} else {
			// 添加產品到購物車
			cart.add(playone);

			// 保存購物車資訊至本地端 JSON
			shoppingService.savePlayoneCartData(memberNum, cart);

			return new ResponseEntity<>("已成功加入商品", HttpStatus.OK);
		}
	}
	
	// 訂票-加入購物車
	@GetMapping(value = "/sean/addTicketToCart" )
	@ResponseBody
	public String addaddTicketToCart(@RequestParam("ticketCartId") int ticketCartId) {  
		// 用 GET 的方法 拿到剛剛丟過來的 cart_id 然後 把相關物件拉出來
		ShoppingCart shoppingCart = ticketsService.findShoppingCartById(ticketCartId);
		// shoppingCart 就包含所有需要的資訊
		/*
		 * 1. 訂票編號 -> shoppingCart.getTicketInfos() => <TicketInfo>.getTicketID();
		 * 2. 出發站/抵達站 (要中文站名)  -> DepartureST_Name/DestinationST_Name
		 * 3. 票價 v  -> shoppingCart.getTicketInfos() => <TicketInfo>.getPrice();
		 * 4. 座位(張數) v ->  shoppingCart.getTicketInfos() => <TicketInfo>.getSeat();
		 * 5. 出發時間 v ->  shoppingCart.getTicketInfos() => <TicketInfo>.getDepartureTime();
		 */
		TicketInfo firstTicketInfo = shoppingCart.getTicketInfos().get(0);
		
		Map<Integer, String> stationMap = highSpeedRailService.getStationInfoMap();
		String DepartureST_Name = stationMap.get(Integer.parseInt(firstTicketInfo.getDepartureST()));
		String DestinationST_Name = stationMap.get(Integer.parseInt(firstTicketInfo.getDestinationST()));
		// 印出來給你看
		System.out.println(shoppingCart.getTicketInfos()); //訂票所有資訊
		System.out.println(firstTicketInfo.getTicketID()); //訂票編號
		System.out.println(DepartureST_Name); 	//出發站(中文)
		System.out.println(DestinationST_Name);	//抵達站(中文)
		System.out.println(firstTicketInfo.getPrice()); //票價
		System.out.println(firstTicketInfo.getSeat()); 	//座位
		System.out.println(firstTicketInfo.getDeparturetime()); //出發時間 
		
		return "";
	}

	// 行程-更新報名人數
	@PutMapping("/sean/updateRegistrations")
	public ResponseEntity<Void> updateRegistrations(@RequestParam("productId") Integer productId,
			@RequestParam("productRegistrations") int productRegistrations, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		String memberNum = member.getMemberNum();

		// 加載購物車資訊
		List<ProductBean> cart = shoppingService.loadProductCartData(memberNum);

		// 更新指定商品的數量
		shoppingService.updateCartQuantity(memberNum, cart, productId, productRegistrations);

		// 儲存更新後的購物車資訊到本地 JSON 檔案
		shoppingService.saveCartData(memberNum, cart);

		return ResponseEntity.ok().build();
	}

	// 行程-刪除購物車行程
	@PostMapping("/sean/RemoveCartItem")
	public String removeCartItem(@RequestParam("productId") Integer productId, HttpSession session) {
		try {
			Member member = (Member) session.getAttribute("mbsession");
			String memberNum = member != null ? member.getMemberNum() : "";

			List<ProductBean> cart = shoppingService.loadProductCartData(memberNum);

			if (cart != null) {
				Iterator<ProductBean> iterator = cart.iterator();
				while (iterator.hasNext()) {
					ProductBean item = iterator.next();
					if (item.getProductId() == productId) {
						iterator.remove();
						break;
					}
				}

				shoppingService.saveCartData(memberNum, cart);
			}

			return "redirect:/sean/CartLoginStatus";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/sean/CartLoginStatus?error=發生錯誤";
		}
	}

	// 旅伴-刪除購物車旅伴
	@PostMapping("/sean/RemoveCartPlayoneItem")
	public String removeCartItem(@RequestParam("playoneId") int playoneId, HttpSession session) {
		try {
			Member member = (Member) session.getAttribute("mbsession");
			String memberNum = member != null ? member.getMemberNum() : "";

			List<Playone> cart = shoppingService.loadPlayoneCartData(memberNum);

			if (cart != null) {
				Iterator<Playone> iterator = cart.iterator();
				while (iterator.hasNext()) {
					Playone item = iterator.next();
					if (item.getPlayoneId() == playoneId) {
						iterator.remove();
						break;
					}
				}

				shoppingService.savePlayoneCartData(memberNum, cart);
			}

			return "redirect:/sean/CartLoginStatus";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/sean/CartLoginStatus?error=發生錯誤";
		}
	}

	// 購物車-結單
	@PostMapping("/sean/checkOut")
	public ResponseEntity<Map<String, String>> checkOut(HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		String memberNum = member.getMemberNum();
		if (memberNum != null) {
			List<ProductBean> productCart = shoppingService.loadProductCartData(memberNum);
			List<Playone> playoneCart = shoppingService.loadPlayoneCartData(memberNum);

			if ((productCart != null && !productCart.isEmpty()) || (playoneCart != null && !playoneCart.isEmpty())) {
				// line pay API
				String transactionId = null;
				String paymentStatus = "尚未付款";

				shoppingService.createOrder(member, productCart, playoneCart, transactionId, paymentStatus);

				// 結單後清除購物車
				productCart.clear();
				playoneCart.clear();
				shoppingService.saveCartData(memberNum, productCart);
				shoppingService.savePlayoneCartData(memberNum, playoneCart);

				return new ResponseEntity<>(Collections.singletonMap("message", "Checkout completed successfully."),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Collections.singletonMap("message", "Cart is empty."), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(Collections.singletonMap("message", "Checkout failed. Please try again."),
				HttpStatus.OK);
	}

	// 訂單-付款
	@PostMapping("/sean/payMoney")
	public ResponseEntity<Map<String, String>> payMoney(@RequestBody Map<String, UUID> payload, Model model,
			HttpSession session) {
		UUID orderId = payload.get("orderId");
		// 獲取會員及訂單資訊
		Member member = (Member) session.getAttribute("mbsession");
		int memberSeq = member.getMemberSeq();
		OrdersBean order = shoppingService.getOrderByIdAndMemberSeq(orderId, memberSeq);

		// 將訂單的 ID 和 totalPrice 和 currency 儲存在 session 中
		session.setAttribute("currentOrderId", order.getId());
		session.setAttribute("totalPrice", order.getTotalPrice());
		session.setAttribute("currency", "TWD");

		// 設置傳遞line pay參數
		LinePayRequest request = new LinePayRequest();
		request.setAmount(order.getTotalPrice());
		request.setCurrency("TWD");
		request.setOrderId(order.getId());
		request.setConfirmUrl("http://localhost:8080/TM/sean/confirmURL"); // 設置 confirmUrl
		request.setProductName("付款測試");

		// 印出確定有無取到值
		System.out.println("訂單id: " + order.getId());
		System.out.println("總價totalprice:" + order.getTotalPrice());

		LinePayResponse response = linePayService.payMoney(request);
		System.out.println("request: " + request.toString());
		System.out.println("response: " + response.toString());

		if (response != null && response.getInfo() != null && response.getInfo().getPaymentUrl() != null) {
			String paymentUrl = response.getInfo().getPaymentUrl().getWeb();
			return new ResponseEntity<>(Collections.singletonMap("paymentUrl", paymentUrl), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.singletonMap("error", "No payment URL found"),
					HttpStatus.BAD_REQUEST);
		}
	}

	// 交易驗證
	@GetMapping("/sean/confirmURL")
	public String confirmURL(@RequestParam String transactionId, HttpSession session,
			RedirectAttributes redirectAttrs) {

		// 從 session 中取出訂單的 totalPrice
		int totalPrice = (int) session.getAttribute("totalPrice");

		System.out.println("totalPrice session: " + totalPrice);

		// 調用 Line Pay API 進行交易驗證
		LinePayStatusResponse paymentStatus = linePayService.checkPaymentStatus(transactionId, totalPrice, "TWD");

		// 根據付款狀態碼進行相應的處理
		if ("0000".equals(paymentStatus.getReturnCode())) {
			// 付款成功，更新訂單狀態
			// 從 session 中取出當前訂單的 ID
			UUID orderId = (UUID) session.getAttribute("currentOrderId");
			OrdersBean order = shoppingService.getOrderById(orderId);
			System.out.println("當前訂單id: " + orderId);
			order.setPaymentStatus("付款成功"); // 將訂單設為已付款
			order.setTransactionId(transactionId);
			order.setPaidAt(LocalDateTime.now());
			shoppingService.updateOrder(order); // 更新訂單狀態

			session.setAttribute("paymentStatus", "Checkout completed!");

			// 返回成功的視圖
			return "redirect:/sean/getPersonalOrder";
		} else {
			// 付款失敗
			session.setAttribute("paymentStatus", "Checkout failed with status: " + paymentStatus.getReturnCode());

			// 返回失敗的視圖
			return "redirect:/sean/paymentError";
		}
	}

	// 全部訂單查詢
	@GetMapping("/sean/getOrder")
	public String getOrder(Model model, HttpSession session) {
		List<OrdersBean> orders = shoppingService.getAllOrders();
		model.addAttribute("orders", orders);

		Member member = (Member) session.getAttribute("mbsession");
		// 有登入
		if (member != null) {
			return "sean/checkoutConfirmation";

			// 沒登入
		} else {
			return "redirect:/login.controller";
		}
	}

	// 個人訂單查詢
	@GetMapping("/sean/getPersonalOrder")
	public String getPersonalOrder(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");

		// 有登入
		if (member != null) {
			int memberSeq = member.getMemberSeq();
			List<OrdersBean> orders = shoppingService.getMemberOrders(memberSeq);
			model.addAttribute("orders", orders);
			return "sean/checkoutConfirmation";
		} else {
			// 沒登入
			return "redirect:/login.controller";
		}
	}

	// 取消訂單
	@DeleteMapping("/sean/deleteOrder/{orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");

		// 有登入
		if (member != null) {
			OrdersBean order = shoppingService.getOrderById(orderId);

			if (order != null) {
				String paymentStatus = order.getPaymentStatus();
				if (paymentStatus.equals("付款成功")) {
					shoppingService.deleteOrder(orderId);
					return ResponseEntity.ok().body(paymentStatus);
				} else {
					shoppingService.deleteOrder(orderId);
					return ResponseEntity.ok().body("Order deleted successfully");
				}
			} else {
				throw new IllegalArgumentException("Invalid order Id: " + orderId);
			}
		} else {
			// 沒登入
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login to delete orders");
		}
	}

	// 搜尋訂單
	@GetMapping("/sean/searchOrder")
	public String searchOrder(@RequestParam("keyword") String keyword, Model model) {
		List<OrdersBean> orders = shoppingService.searchOrders(keyword);
		model.addAttribute("orders", orders);
		return "sean/checkoutConfirmation";
	}
}