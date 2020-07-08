package com.victor.backend.projects.orderSystem.pojo.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.victor.backend.projects.orderSystem.exception.GeneralValidationException;
import com.victor.backend.projects.orderSystem.pojo.service.Coordination;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PlaceOrderReq {
    private List<String> origin = Collections.emptyList();
    private List<String> destination = Collections.emptyList();

    @JsonIgnore
    public Coordination getOriginCoor() {
        return getCoordination(origin, "origin");
    }

    @JsonIgnore
    public Coordination getDestCoor() {
        return getCoordination(destination, "destination");
    }

    @JsonIgnore
    public Coordination getCoordination(List<String> list, String typeOfCoor) {
        if(list.size() != 2) throw new GeneralValidationException("Missing coordination(s) for " + typeOfCoor);

        return Coordination.builder()
                .latitude(list.get(0))
                .longitude(list.get(1))
                .build();
    }
}
