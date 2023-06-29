// 購物車動態抓取資訊
const quantityFields = document.querySelectorAll('.quantity-field');
const totalAmountElement = document.querySelector('#basket-total');
const totalItemsElement = document.querySelector('.total-items');

// 更新購物車總金額和總數量的函數
function updateCartTotal() {
	let totalAmount = 0;
	let totalItems = 0;

	quantityFields.forEach((quantityField) => {
		const quantity = parseInt(quantityField.value);
		const priceElement = quantityField.parentNode.querySelector('[name="productPrice"]');
		const price = parseFloat(priceElement.value);

		const subtotal = quantity * price;

		// 更新對應的小計元素
		const subtotalElement = quantityField.parentNode.parentNode.querySelector('.subtotal');
		subtotalElement.textContent = subtotal;

		totalAmount += subtotal;
		totalItems += quantity;
	});

	totalAmountElement.textContent = totalAmount;
	totalItemsElement.textContent = totalItems;
}

// 初始化購物車總金額
updateCartTotal();

// 監聽數量輸入框變化事件（事件代理）
const cartItems = document.querySelector('.cart-items');
cartItems.addEventListener('input', (event) => {
	if (event.target.classList.contains('quantity-field')) {
		updateCartTotal();
	}
});



//輸入優惠碼
var promoCodeInput = document.getElementById('promo-code');

promoCodeInput.addEventListener('input', function() {
	if (promoCodeInput.value !== '') {
		promoCodeInput.placeholder = '';
	}
});

promoCodeInput.addEventListener('blur', function() {
	if (promoCodeInput.value === '') {
		promoCodeInput.placeholder = '輸入優惠碼:';
	}
});


//動態修改商品數量(判斷不能大於庫存)-傳後端
$(".quantity-field").change(function() {
	console.log($(this).val());
	let val = parseInt($(this).val());
	let productId = $(this).closest(".basket-product").find(".productId").val();
	let max = parseInt($(this).siblings(".productQuantity").val());

	if (val <= max) {
		$.ajax({
			url: "http://localhost:8080/TM/sean/updateRegistrations",
			type: "put",
			data: {
				"productId": productId,
				"productRegistrations": $(this).val()
			},
			success: function() {
				console.log("成功修改");
			},
			error: function() {
				console.log('失敗');
			}
		});
	} else {
		console.log("失敗?");
	}
});
