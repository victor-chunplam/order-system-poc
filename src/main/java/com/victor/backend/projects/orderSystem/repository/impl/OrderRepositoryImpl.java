package com.victor.backend.projects.orderSystem.repository.impl;

import com.victor.backend.projects.orderSystem.db.enums.OrderStatus;
import com.victor.backend.projects.orderSystem.db.tables.daos.OrderDao;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import com.victor.backend.projects.orderSystem.repository.OrderRepository;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.victor.backend.projects.orderSystem.db.Tables.ORDER;

@Repository
public class OrderRepositoryImpl extends OrderDao implements OrderRepository {

    private DefaultDSLContext dsl;

    @Autowired
    public OrderRepositoryImpl(DefaultDSLContext dsl) {
        super.setConfiguration(dsl.configuration());
        this.dsl = dsl;
    }

    @Override
    public List<OrderInfo> getOrders(BigInteger limit, BigInteger offset) {
        if(Stream.of(limit, offset).anyMatch(x -> Objects.isNull(x) || x.compareTo(BigInteger.ZERO) < 0))
            throw new IllegalArgumentException("Invalid limit and offset - limit: " + limit + ", offset: " + offset);

        return this.dsl
                .select(ORDER.ID, ORDER.DISTANCE, ORDER.STATUS)
                .from(ORDER)
                .limit(limit)
                .offset(offset)
                .fetch()
                .stream()
                .map(it ->
                        OrderInfo.builder()
                                .id(it.component1())
                                .distance(it.component2())
                                .status(it.component3())
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<OrderInfo> getOrders() {
        return this.dsl
                .select(ORDER.ID, ORDER.DISTANCE, ORDER.STATUS)
                .from(ORDER)
                .fetch()
                .stream()
                .map(it ->
                        OrderInfo.builder()
                                .id(it.component1())
                                .distance(it.component2())
                                .status(it.component3())
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    public int updateStatus(Long id, OrderStatus oldStatus, OrderStatus newStatus) {
        if(Stream.of(id, oldStatus, newStatus).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Invalid id, oldStatus and newStatus " +
                    "- id: " + id +
                    ", oldStatus: " + oldStatus +
                    ", newStatus: " + newStatus);

        return this.dsl
                .update(ORDER)
                .set(ORDER.STATUS, newStatus)
                .where(ORDER.STATUS.eq(oldStatus).and(ORDER.ID.eq(id)))
                .execute();
    }
}
