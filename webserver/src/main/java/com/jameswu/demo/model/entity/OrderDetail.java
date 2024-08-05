package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table
@Entity(name = "order_detail")
@Getter
@Setter
public class OrderDetail implements Serializable {

	@Id
	@Column(name = "order_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	private Product product;

	@Column(nullable = false)
	private int quantity;

	@Column(name = "coupon_code", nullable = true)
	private UUID couponCode;

	@Column(name = "payment", precision = 11, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Payment must be greater than or equal to 0.00")
	private BigDecimal payment;

	@Column(name = "discount", precision = 11, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Payment must be greater than or equal to 0.00")
	private BigDecimal discount;

	public OrderDetail() {}

	public OrderDetail(
			Product product, Order order, int quantity, BigDecimal payment, UUID couponCode, BigDecimal discount) {
		this.product = product;
		this.quantity = quantity;
		this.payment = payment;
		this.order = order;
		this.couponCode = couponCode;
		this.discount = discount;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		OrderDetail that = (OrderDetail) object;
		return orderDetailId == that.orderDetailId
				&& quantity == that.quantity
				&& Objects.equals(order, that.order)
				&& Objects.equals(product, that.product)
				&& Objects.equals(couponCode, that.couponCode)
				&& Objects.equals(payment, that.payment)
				&& Objects.equals(discount, that.discount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderDetailId, order, product, quantity, couponCode, payment, discount);
	}
}
