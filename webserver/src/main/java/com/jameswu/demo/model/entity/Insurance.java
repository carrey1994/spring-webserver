package com.jameswu.demo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraph(
        name = "insurance_graph",
        attributeNodes = {@NamedAttributeNode("order")})
@Entity(name = "insurance")
@Table
@NoArgsConstructor
@Getter
@Setter
public class Insurance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "insurance")
    private Set<InsuranceOrder> order;

    public Insurance(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Insurance insurance = (Insurance) object;
        return insuranceId == insurance.insuranceId
                && Objects.equals(title, insurance.title)
                && Objects.equals(description, insurance.description)
                && Objects.equals(order, insurance.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceId, title, description, order);
    }
}
