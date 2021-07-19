package com.p.sx.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDERS")
@ToString(exclude = "items")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_ORDER", nullable = false, unique = true)
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "DAT_CREATION", nullable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy="order", orphanRemoval=true, cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }


    public void add(final List<OrderItemEntity> itemsList) {
        if(Objects.isNull(items)){
            items = new ArrayList<>();
        }

        for (OrderItemEntity item: itemsList) {
            item.setOrder(this);
            items.add(item);
        }
    }
}
