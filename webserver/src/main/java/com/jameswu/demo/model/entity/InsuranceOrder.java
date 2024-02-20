package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedEntityGraph(
        name = "insurance_order_graph",
        attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("insurance")})
@Entity(name = "insurance_order")
@Table
@Data
@NoArgsConstructor
public class InsuranceOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private GcUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @Column(name = "cart_id", nullable = false)
    private long cartId;

    public InsuranceOrder(GcUser user, Insurance insurance) {
        this.user = user;
        this.insurance = insurance;
    }
}
