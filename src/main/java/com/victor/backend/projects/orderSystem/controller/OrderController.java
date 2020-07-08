package com.victor.backend.projects.orderSystem.controller;

import com.victor.backend.projects.orderSystem.pojo.req.PlaceOrderReq;
import com.victor.backend.projects.orderSystem.pojo.req.TakeOrderReq;
import com.victor.backend.projects.orderSystem.pojo.resp.TakeOrderResp;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import com.victor.backend.projects.orderSystem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderInfo> placeOrder(@RequestBody PlaceOrderReq req) {
        return ResponseEntity
                .ok()
                .body(orderService.placeOrder(req.getOriginCoor(), req.getDestCoor()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TakeOrderResp> takeOrder(@RequestBody TakeOrderReq req,
                                                   @PathVariable String id) {
        orderService.takeOrder(req.getStatus(), id);
        return ResponseEntity
                .ok()
                .body(TakeOrderResp
                        .builder()
                        .status(TakeOrderResp.TakeOrderStatus.SUCCESS)
                        .build());
    }

    @GetMapping
    public ResponseEntity<List<OrderInfo>> getOrders(@RequestParam(required = false) String page,
                                                     @RequestParam(required = false) String limit) {
        return ResponseEntity
                .ok()
                .body(orderService.getOrders(page, limit));
    }

}
