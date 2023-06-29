package com.tm.TravelMaster.sean.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.TravelMaster.sean.model.ProductBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShoppingService {

	// JSON儲存路徑
	private static final String CART_DATA_DIRECTORY = "C:\\TravelMaster\\workspace\\TravelMaster\\src\\data\\cart_data";

	// 載入購物車資訊-JSON
	public List<ProductBean> loadCartData(String memberNum) {
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

	// 儲存購物車資訊至本地端-JSON
	public void saveCartData(String memberNum, List<ProductBean> cart) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			File file = new File(CART_DATA_DIRECTORY + File.separator + memberNum + ".json");
			objectMapper.writeValue(file, cart);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 購物車-重複產品判斷
	public boolean isProductInCart(List<ProductBean> cart, Integer integer) {
		for (ProductBean item : cart) {
			if (item.getProductId().equals(integer)) {
				return true;
			}
		}
		return false;
	}

	// 購物車-更新JSON商品數量
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

	// 購物車-將JSON資料寫進資料庫

}