package com.jameswu.demo.model.entity;

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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
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
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID couponId;

    @Column(name = "payment", precision = 10, scale = 4, nullable = false)
    private BigDecimal payment;

    public OrderDetail() {}

    public OrderDetail(Product product, Order order, int quantity, BigDecimal payment, UUID couponId) {
        this.product = product;
        this.quantity = quantity;
        this.payment = payment;
        this.order = order;
        this.couponId = couponId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderDetail that = (OrderDetail) object;
        return quantity == that.quantity
                && Objects.equals(orderDetailId, that.orderDetailId)
                && Objects.equals(order, that.order)
                && Objects.equals(product, that.product)
                && Objects.equals(couponId, that.couponId)
                && Objects.equals(payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDetailId, order, product, quantity, couponId, payment);
    }
}
