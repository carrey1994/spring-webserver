package com.jameswu.demo.sales;

import com.jameswu.demo.model.entity.OrderDetail;
import com.jameswu.demo.repository.CouponRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesCouponFactory {
	@Autowired
	private CouponRepository couponRepository;

	public void calculateDiscountByCoupon(OrderDetail orderDetail) {
		var coupon = couponRepository.findById(orderDetail.getCouponId()).orElse(null);
		var productPrice = orderDetail.getProduct().getPrice();
		if (coupon == null) {
			orderDetail.setDiscount(BigDecimal.ZERO);
			orderDetail.setPayment(productPrice);
			return;
		}
		switch (coupon.getCouponCategory()) {
			case CASH_DISCOUNT:
				orderDetail.setDiscount(coupon.getCashDiscount());
				orderDetail.setPayment(productPrice.min(coupon.getCashDiscount()));
				break;
			case PERCENTAGE_DISCOUNT:
				orderDetail.setDiscount(productPrice.multiply(coupon.getPercentageDiscount()));
				orderDetail.setPayment(productPrice.min(productPrice.multiply(coupon.getPercentageDiscount())));
				break;
			default:
				break;
		}
	}
}
