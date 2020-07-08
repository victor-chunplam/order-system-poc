package com.victor.backend.projects.orderSystem.pojo.restClient.resp;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GoogleMapDistanceMatrixResp {
    @SerializedName("destination_addresses")
    private List<String> destinationAddresses = Collections.emptyList();

    @SerializedName("origin_addresses")
    private List<String> originAddresses = Collections.emptyList();

    private List<Row> rows = Collections.emptyList();

    private String status;

    @Data
    public static class Row {
        private List<RowElement> elements = Collections.emptyList();
    }

    @Data
    public static class RowElement {
        private RowElementItem distance;
        private RowElementItem duration;

        private String status;
    }

    @Data
    public static class RowElementItem {
        private String text;
        private Integer value;
    }
}
