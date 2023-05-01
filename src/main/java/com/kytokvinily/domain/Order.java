package com.kytokvinily.domain;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("orders")
public record Order(

        @Id
        Long id,
        Long vinylId,
        String vinylTitle,
        String vinylAuthor,
        Double vinylPrice,
        Integer quantity,
        OrderStatus orderStatus,
        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @CreatedBy
        String createdBy,
        @LastModifiedBy
        String lastModifiedBy,
        @Version
        int version
) {
    public static Order of(
            Long vinylId, String vinylTitle, String vinylAuthor, Double vinylPrice, Integer quantity, OrderStatus orderStatus
    ) {
        return new Order(null, vinylId, vinylTitle, vinylAuthor, vinylPrice, quantity, orderStatus, null, null, null, null, 0);
    }
}
