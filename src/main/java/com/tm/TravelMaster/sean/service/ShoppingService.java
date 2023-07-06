package com.tm.TravelMaster.sean.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.leo.model.Playone;
import com.tm.TravelMaster.ming.model.entity.TicketInfoGroup;
import com.tm.TravelMaster.sean.model.OrdersBean;
import com.tm.TravelMaster.sean.model.OrderItemsBean;
import com.tm.TravelMaster.sean.model.ProductBean;
import com.tm.TravelMaster.sean.repository.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ShoppingService {

	// JSON儲存路徑 
	private static final String CART_DATA_DIRECTORY = "D:\\TravelMaster\\TravelMaster\\src\\data\\cart_data";

	// 導入JPA
	private final OrdersRepository ordersRepository;
	private final OrderItemsRepository orderItemsRepository;

	public ShoppingService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository) {
		this.ordersRepository = ordersRepository;
		this.orderItemsRepository = orderItemsRepository;
	}

	// 行程-載入購物車JSON
	public List<ProductBean> loadProductCartData(String memberNum) {
		List<ProductBean> cart = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + ".json");
			if (file.exists()) {
				cart = objectMapper.readValue(file, new TypeReference<List<ProductBean>>() {
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cart;
	}

	// 旅伴-載入購物車JSON
	public List<Playone> loadPlayoneCartData(String memberNum) {
		List<Playone> cart = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + "_playone.json");
			if (file.exists()) {
				cart = objectMapper.readValue(file, new TypeReference<List<Playone>>() {
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cart;
	}

	// 訂票-載入購物車JSON
	public List<TicketInfoGroup> loadTicketCartData(String memberNum) {
		List<TicketInfoGroup> cart = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + "_Ticket.json");
			if (file.exists()) {
				cart = objectMapper.readValue(file, new TypeReference<List<TicketInfoGroup>>() {
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cart;
	}

	// 行程-儲存購物車JSON資訊至本地端
	public void saveCartData(String memberNum, List<ProductBean> cart) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + ".json");
			objectMapper.writeValue(file, cart);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 旅伴-儲存購物車JSON資訊至本地端
	public void savePlayoneCartData(String memberNum, List<Playone> cart) {
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + "_playone.json");
		try {
			objectMapper.writeValue(file, cart);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 訂票-儲存購物車JSON資訊至本地端
	public void saveTicketCartData(String memberNum, List<TicketInfoGroup> cart) {
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + "_Ticket.json");
		try {
			objectMapper.writeValue(file, cart);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 行程-重複行程判斷
	public boolean isProductInCart(List<ProductBean> cart, Integer integer) {
		for (ProductBean item : cart) {
			if (item.getProductId().equals(integer)) {
				return true;
			}
		}
		return false;
	}

	// 旅伴-重複旅伴判斷
	public boolean isPlayoneInCart(List<Playone> cart, int playoneId) {
		return cart.stream().anyMatch(playone -> playone.getPlayoneId() == playoneId);
	}

	// 訂票-重複訂票判斷
	public boolean isCartExists(List<TicketInfoGroup> carts, int cartId) {
		for (TicketInfoGroup cart : carts) {
			if (cart.getCart_Id() == cartId) {
				return true;
			}
		}
		return false;
	}

	// 行程-更新JSON商品數量
	public void updateCartQuantity(String memberNum, List<ProductBean> cart, Integer productId,
			int productRegistrations) {
		for (ProductBean product : cart) {
			if (product.getProductId().equals(productId)) {
				product.setProductRegistrations(productRegistrations);
				break;
			}
		}
		saveCartData(memberNum, cart);
	}

	// 購物車-將JSON資料寫進資料庫結單
	public OrdersBean createOrder(Member member, List<ProductBean> productCart, List<Playone> playoneCart,
			String transactionId, String paymentStatus) {
		OrdersBean orders = new OrdersBean();
		orders.setMemberSeq(member.getMemberSeq());

		// 計算總金額
		int totalPrice = 0;
		for (ProductBean product : productCart) {
			totalPrice += product.getProductPrice() * product.getProductRegistrations();
		}
		int playonePrice = 3000; // 假設每個陪玩的價格固定為 3000
		totalPrice += playonePrice * playoneCart.size();

		// 設值
		orders.setTotalPrice(totalPrice);
		orders.setTransactionId(transactionId);
		orders.setPaymentStatus(paymentStatus);
		orders.setCreatedAt(LocalDateTime.now());

		orders = ordersRepository.save(orders);

		for (ProductBean product : productCart) {
			OrderItemsBean orderItems = new OrderItemsBean();
			orderItems.setOrders(orders);
			orderItems.setProductId(product.getProductId());
			orderItems.setProductName(product.getProductName());
			orderItems.setProductPrice(product.getProductPrice());
			orderItems.setQuantity(product.getProductRegistrations());
			orderItems.setSubTotal(product.getProductPrice() * product.getProductRegistrations());
			orderItemsRepository.save(orderItems);
		}

		for (Playone playone : playoneCart) {
			OrderItemsBean orderItems = new OrderItemsBean();
			orderItems.setOrders(orders);
			orderItems.setPlayoneId(playone.getPlayoneId());
			orderItems.setPlayoneNick(playone.getPlayoneNick());
			orderItems.setPlayonePrice(3000);
			orderItems.setQuantity(1); // 假設陪玩項目的數量為 1
			orderItems.setSubTotal(3000);
			orderItemsRepository.save(orderItems);
		}

		return orders;
	}

	// 全部訂單查詢
	public List<OrdersBean> getAllOrders() {
		return ordersRepository.findAll();
	}

	// 個人訂單查詢
	public List<OrdersBean> getMemberOrders(int memberSeq) {
		return ordersRepository.findByMemberSeq(memberSeq);
	}

	// 設定付款選取訂單
	public OrdersBean getOrderByIdAndMemberSeq(UUID orderId, int memberSeq) {
		return ordersRepository.findByIdAndMemberSeq(orderId, memberSeq);
	}

	// 取單筆訂單ID
	public OrdersBean getOrderById(UUID orderId) {
		return ordersRepository.findById(orderId);
	}

	// 更新訂單狀態
	public OrdersBean updateOrder(OrdersBean order) {
		return ordersRepository.save(order);
	}

	// 刪除訂單
	public void deleteOrder(UUID orderId) {
		OrdersBean order = ordersRepository.findById(orderId);
		if (order != null) {
			// 删除订单项
			List<OrderItemsBean> orderItems = order.getItems();
			orderItems.forEach(orderItem -> ordersRepository.deleteOrderItem(orderItem));

			// 删除订单
			ordersRepository.delete(order);
		} else {
			throw new IllegalArgumentException("Invalid order Id:" + orderId);
		}
	}

	// 搜尋訂單
	public List<OrdersBean> searchOrders(String keyword) {
		return ordersRepository.searchOrders(keyword);
	}
}