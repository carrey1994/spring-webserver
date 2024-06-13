package com.jameswu.demo.model.entity;

import static com.jameswu.demo.model.enums.DiscountCategory.CASH_DISCOUNT;
import static com.jameswu.demo.model.enums.DiscountCategory.PERCENTAGE_DISCOUNT;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jameswu.demo.model.enums.DiscountCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "coupon")
@Table
@Getter
@Setter
@EqualsAndHashCode
public class Coupon implements Serializable {

	@Id
	private int couponId;

	@Column(nullable = false, unique = true)
	@NotNull private String couponCode;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	@EqualsAndHashCode.Exclude
	private GcUser user;

	@Enumerated(value = EnumType.STRING)
	@NotNull private DiscountCategory couponCategory;

	@Column(nullable = false)
	private String description;

	@Column(name = "cash_discount", precision = 10, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Cash Discount must be greater than or equal to 0.01")
	private BigDecimal cashDiscount;

	@Column(name = "percentage_discount", precision = 3, scale = 2, nullable = false)
	@DecimalMin(value = "0.01", message = "Off Discount must be greater than or equal to 0.01")
	@DecimalMax(value = "0.99", message = "Off Discount must be less than or equal to 0.99")
	private BigDecimal percentageDiscount;

	public Coupon() {}

	public static Coupon cashCoupon(BigDecimal cashDiscount, String description, GcUser user, String couponCode) {
		return new Coupon(cashDiscount, BigDecimal.ZERO, description, CASH_DISCOUNT, user, couponCode);
	}

	public static Coupon percentageCoupon(
			BigDecimal percentageDiscount, String description, GcUser user, String couponCode) {
		return new Coupon(BigDecimal.ZERO, percentageDiscount, description, PERCENTAGE_DISCOUNT, user, couponCode);
	}

	private Coupon(
			BigDecimal cashDiscount,
			BigDecimal percentageDiscount,
			String description,
			DiscountCategory couponCategory,
			GcUser user,
			String couponCode) {
		this.description = description;
		this.cashDiscount = cashDiscount;
		this.percentageDiscount = percentageDiscount;
		this.couponCategory = couponCategory;
	}
}
