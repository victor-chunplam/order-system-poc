package com.victor.backend.projects.orderSystem.service;

import com.victor.backend.projects.orderSystem.annotation.CheckString;
import com.victor.backend.projects.orderSystem.annotation.IsIntegerInRange;
import com.victor.backend.projects.orderSystem.pojo.service.Coordination;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface OrderService {
    @Transactional
    List<OrderInfo> getOrders(@IsIntegerInRange(message = "'page' must be an integer starting with 1", min = "1", max = "9223372036854775807") String page,
                              @IsIntegerInRange(message = "'limit' must be an integer starting with 1", min = "1", max = "9223372036854775807") String limit);

    @Transactional
    OrderInfo placeOrder(@Valid Coordination origin, @Valid Coordination destination);

    @Transactional
    void takeOrder(@NotBlank
                   @CheckString(checkingValues = {"TAKEN"}, message = "Invalid order status inputted!")
                           String orderStatus,
                   @NotBlank
                   @IsIntegerInRange(message = "'id' must be an integer", min = "1", max = "9223372036854775807")
                           String id);
}
