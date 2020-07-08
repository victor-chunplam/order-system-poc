package com.victor.backend.projects.orderSystem.pojo.service;

import com.victor.backend.projects.orderSystem.db.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfo {
    private Long id;
    private Integer distance;
    private OrderStatus status;
}
