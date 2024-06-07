package com.jameswu.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "product")
@Table(indexes = {@Index(name = "idx_price", columnList = "price", unique = true)})
@NoArgsConstructor
@Getter
@Setter
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "product_id", nullable = false)
	private int productId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "price", precision = 10, scale = 2, nullable = false)
	@DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
	private BigDecimal price;

	@Column(name = "booked", nullable = false)
	private int quantity;

	public Product(String title, String description, BigDecimal price, int quantity) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

	public Product(int productId, String title, String description, BigDecimal price, int quantity) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.productId = productId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Product product = (Product) object;
		return productId == product.productId
				&& quantity == product.quantity
				&& Objects.equals(title, product.title)
				&& Objects.equals(description, product.description)
				&& Objects.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, title, description, price, quantity);
	}
}
