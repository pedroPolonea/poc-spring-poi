package com.p.sx.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDER_ITEMS")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_ORDER_ITEMS", nullable = false, unique = true)
    private Long id;

    @Column(name = "LINE", nullable = false)
    private int line;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "HASH", nullable = false)
    private String hash;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "DAT_CREATION", nullable = false)
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name="ID_ORDER", nullable=false)
    private OrderEntity order;


    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }
}
