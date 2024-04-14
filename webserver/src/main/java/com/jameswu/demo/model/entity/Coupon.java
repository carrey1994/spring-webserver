package com.jameswu.demo.model.entity;

import com.jameswu.demo.model.enums.DiscountCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
	private BigDecimal offDiscount;
}
