package com.victor.backend.projects.orderSystem.repository;

import com.victor.backend.projects.orderSystem.db.enums.OrderStatus;
import com.victor.backend.projects.orderSystem.db.tables.pojos.Order;
import com.victor.backend.projects.orderSystem.db.tables.records.OrderRecord;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import org.jooq.DAO;

import java.math.BigInteger;
import java.util.List;

public interface OrderRepository extends DAO<OrderRecord, Order, Long> {
    List<OrderInfo> getOrders(BigInteger limit, BigInteger offset);

    List<OrderInfo> getOrders();

    int updateStatus(Long id, OrderStatus oldStatus, OrderStatus newStatus);
}
