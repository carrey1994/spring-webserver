package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table
@Entity
@Data
public class SalesEvent implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sales_event_id", nullable = false)
	private int salesEventId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "product_sales_event",
			joinColumns = {@JoinColumn(name = "sales_event_id")},
			inverseJoinColumns = {@JoinColumn(name = "product_id")})
	@EqualsAndHashCode.Exclude
	private Set<Product> products;

	@Column(name = "discount", precision = 10, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Discount must be greater than or equal to 0.00")
	private BigDecimal discount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	@Column(name = "start_day", nullable = false)
	private Instant startDay;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	@Column(name = "end_day", nullable = false)
	private Instant endDay;
}
