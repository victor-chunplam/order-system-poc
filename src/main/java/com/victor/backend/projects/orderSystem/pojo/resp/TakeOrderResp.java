package com.victor.backend.projects.orderSystem.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TakeOrderResp {
    private TakeOrderStatus status;

    public enum TakeOrderStatus {
        SUCCESS
    }

}
