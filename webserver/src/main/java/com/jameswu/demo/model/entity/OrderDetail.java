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
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table
@Entity(name = "order_detail")
@Data
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	private Product product;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = true, columnDefinition = "BINARY(16)")
	private UUID couponId;

	@Column(name = "payment", precision = 10, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Payment must be greater than or equal to 0.00")
	private BigDecimal payment;

	public OrderDetail() {}

	public OrderDetail(
			Product product, Order order, int quantity, BigDecimal payment, UUID couponId) {
		this.product = product;
		this.quantity = quantity;
		this.payment = payment;
		this.order = order;
		this.couponId = couponId;
	}
}
