package com.victor.backend.projects.orderSystem.service.impl;

import com.victor.backend.projects.orderSystem.annotation.CheckString;
import com.victor.backend.projects.orderSystem.annotation.IsIntegerInRange;
import com.victor.backend.projects.orderSystem.db.enums.OrderStatus;
import com.victor.backend.projects.orderSystem.db.tables.pojos.Order;
import com.victor.backend.projects.orderSystem.exception.GeneralSystemException;
import com.victor.backend.projects.orderSystem.exception.GeneralValidationException;
import com.victor.backend.projects.orderSystem.exception.OrderHasTakenException;
import com.victor.backend.projects.orderSystem.pojo.restClient.resp.GoogleMapDistanceMatrixResp;
import com.victor.backend.projects.orderSystem.pojo.service.Coordination;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import com.victor.backend.projects.orderSystem.repository.OrderRepository;
import com.victor.backend.projects.orderSystem.restClient.GoogleMapRestApiRepository;
import com.victor.backend.projects.orderSystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

    @Value("${google.api.key}")
    String apiKey = "";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GoogleMapRestApiRepository googleMapRestApiRepository;


    @Transactional
    @Override
    public List<OrderInfo> getOrders(@IsIntegerInRange(message = "'page' must be an integer starting with 1", min = "1", max = "9223372036854775807") String page,
                                     @IsIntegerInRange(message = "'limit' must be an integer starting with 1", min = "1", max = "9223372036854775807") String limit)
    {
        if(limit == null) {
            return orderRepository.getOrders();
        } else {
            BigInteger pageLong = Optional.ofNullable(page)
                    .map(BigInteger::new)
                    .orElse(BigInteger.ONE);
            BigInteger limitLong = new BigInteger(limit);
            BigInteger offset = (pageLong.subtract(BigInteger.ONE)).multiply(limitLong);
            checkNumWithLongMaxVal(offset, "'page'/'limit'");

            return orderRepository.getOrders(limitLong, offset);
        }
    }

    private void checkNumWithLongMaxVal(BigInteger bi, String name) {
        if(bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) >= 1) {
            throw new GeneralValidationException(name + " is too large!");
        }
    }

    private Map<String, Object> prepareMapApiUrlParam(Coordination origin, Coordination destination) {
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("units", "imperial");
        urlParams.put("key", apiKey);
        urlParams.put("origins", String.join(",", origin.getLatitude(), origin.getLongitude()));
        urlParams.put("destinations", String.join(",", destination.getLatitude(), destination.getLongitude()));

        return urlParams;
    }

    @Transactional
    @Override
    public OrderInfo placeOrder(@Valid Coordination origin, @Valid Coordination destination) {
        Integer distance;
        try {
            GoogleMapDistanceMatrixResp resp = googleMapRestApiRepository.postDistanceMatrix(
                    prepareMapApiUrlParam(origin, destination))
                    .execute()
                    .body();

            distance = Optional.ofNullable(resp)
                    .map(GoogleMapDistanceMatrixResp::getRows)
                    .orElse(new ArrayList<>())
                    .stream()
                    .findFirst()
                    .map(GoogleMapDistanceMatrixResp.Row::getElements)
                    .orElse(new ArrayList<>())
                    .stream()
                    .findFirst()
                    .map(GoogleMapDistanceMatrixResp.RowElement::getDistance)
                    .map(GoogleMapDistanceMatrixResp.RowElementItem::getValue)
                    .orElseThrow(() -> new GeneralSystemException("Cannot get distance from Google Map API"));
        } catch(IOException e) {
            throw new GeneralSystemException("Cannot call Google Map API", e);
        }

        Order order = new Order(null,
                origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude(),
                distance, OrderStatus.UNASSIGNED,
                LocalDateTime.now(), LocalDateTime.now());
        orderRepository.insert(order);

        return OrderInfo.builder()
                .distance(order.getDistance())
                .id(order.getId())
                .status(order.getStatus())
                .build();
    }

    @Transactional
    @Override
    public void takeOrder(@NotBlank
                              @CheckString(checkingValues = {"TAKEN"}, message = "Invalid order status inputted!")
                                      String orderStatus,
                          @NotBlank
                          @IsIntegerInRange(message = "'id' must be an integer", min = "1", max = "9223372036854775807")
                                  String id) {
        int resultNum = orderRepository.updateStatus(
                Long.parseLong(id),
                OrderStatus.UNASSIGNED,
                OrderStatus.TAKEN);

        if(resultNum != 1) {
            throw new OrderHasTakenException("The order has already been taken by someone else or the id is invalid!");
        }
    }

}
