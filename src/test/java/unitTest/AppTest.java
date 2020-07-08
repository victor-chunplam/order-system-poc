package unitTest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.backend.projects.orderSystem.App;
import com.victor.backend.projects.orderSystem.db.enums.OrderStatus;
import com.victor.backend.projects.orderSystem.pojo.req.PlaceOrderReq;
import com.victor.backend.projects.orderSystem.pojo.req.TakeOrderReq;
import com.victor.backend.projects.orderSystem.pojo.service.OrderInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import unitTest.helper.Helpers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mvc;

    final private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetOrderListSuccessWithPageAndLimit() throws Exception {
        mvc.perform(get("/orders?page=1&limit=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(Helpers.loadFile("get-order-list-with-page-and-limit.json")));
    }

    @Test
    public void testGetOrderListSuccessWithLimitOnly() throws Exception {
        mvc.perform(get("/orders?limit=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(Helpers.loadFile("get-order-list-with-page-and-limit.json")));
    }

    @Test
    public void testGetOrderListSuccessWithPageOnly() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/orders?page=1"))
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory().
                constructCollectionType(List.class, OrderInfo.class);
        List<OrderInfo> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), type);
        for(OrderInfo orderInfo : results) {
            assertTrue(orderInfo.getId() != null && orderInfo.getId() >= 0);
            assertNotNull(orderInfo.getStatus());
            assertTrue(orderInfo.getDistance() != null && orderInfo.getDistance() >= 0);
        }
    }

    @Test
    public void testGetOrderListSuccess() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/orders"))
            .andExpect(status().isOk())
            .andReturn();

        JavaType type = objectMapper.getTypeFactory().
                constructCollectionType(List.class, OrderInfo.class);
        List<OrderInfo> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), type);
        for(OrderInfo orderInfo : results) {
            assertTrue(orderInfo.getId() != null && orderInfo.getId() >= 0);
            assertNotNull(orderInfo.getStatus());
            assertTrue(orderInfo.getDistance() != null && orderInfo.getDistance() >= 0);
        }
    }

    @Test
    public void testGetOrderListFailWithPageLessThanOne() throws Exception {
        mvc.perform(get("/orders?page=0&limit=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("get-order-list-invalid-page.json")));
    }

    @Test
    public void testGetOrderListFailWithNonNumericPage() throws Exception {
        mvc.perform(get("/orders?page=a&limit=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("get-order-list-invalid-page.json")));
    }

    @Test
    public void testGetOrderListFailWithLimitLessThanOne() throws Exception {
        mvc.perform(get("/orders?page=1&limit=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("get-order-list-invalid-limit.json")));
    }

    @Test
    public void testGetOrderListFailWithNonNumericLimit() throws Exception {
        mvc.perform(get("/orders?page=1&limit=a"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("get-order-list-invalid-limit.json")));
    }

    @Test
    public void testPlaceOrderAndTakeOrderSuccess() throws Exception {
        PlaceOrderReq placeOrderReq = new PlaceOrderReq();
        placeOrderReq.setOrigin(Arrays.asList("40.6655101", "-73.891889699999"));
        placeOrderReq.setDestination(Arrays.asList("41.432", "-81.3899"));

        ResultActions resultActions = mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(placeOrderReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("id").value(greaterThanOrEqualTo(0), Integer.class))
                .andExpect(jsonPath("distance").exists())
                .andExpect(jsonPath("distance").value(equalTo(740935), Integer.class))
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("status").value(OrderStatus.UNASSIGNED.toString()))
                ;
        String resp = resultActions.andReturn().getResponse().getContentAsString();

        {
            OrderInfo orderInfo = objectMapper.readValue(resp, OrderInfo.class);
            TakeOrderReq takeOrderReq = new TakeOrderReq();
            takeOrderReq.setStatus(OrderStatus.TAKEN.toString());

            mvc.perform(patch("/orders/" + orderInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(takeOrderReq)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(Helpers.loadFile("take-order-success.json")));
        }
    }

    @Test
    public void testPlaceOrderFailWithNoDistanceGotFromGoogle() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90", "-180"));
        req.setDestination(Arrays.asList("-90", "-180"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(Helpers.loadFile("place-order-no-distance-got-from-google.json")));
    }

    @Test
    public void testPlaceOrderFailWithMissingOriginCoordination() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90"));
        req.setDestination(Arrays.asList("-90", "-180"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-missing-origin.json")));
    }

    @Test
    public void testPlaceOrderFailWithMissingDestCoordination() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90", "-180"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-missing-destination.json")));
    }

    @Test
    public void testPlaceOrderFailWithInvalidLatitude() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90.00001", "-180"));
        req.setDestination(Arrays.asList("-90", "-180"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-invalid-latitude.json")));
    }

    @Test
    public void testPlaceOrderFailWithNonNumbeericLatitude() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("abc", "-180"));
        req.setDestination(Arrays.asList("-90", "-180"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-invalid-latitude.json")));
    }

    @Test
    public void testPlaceOrderFailWithInvalidLongitude() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90.0000", "-180"));
        req.setDestination(Arrays.asList("-90", "180.00001"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-invalid-longitude.json")));
    }

    @Test
    public void testPlaceOrderFailWithNonNumericLongitude() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList("-90.0000", "-180"));
        req.setDestination(Arrays.asList("-90", "asdas"));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-invalid-longitude.json")));
    }

    @Test
    public void testPlaceOrderFailWithEmptyCoordination() throws Exception {
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrigin(Arrays.asList(null, "-180"));
        req.setDestination(Arrays.asList("-90", null));

        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("place-order-fail-with-empty-coor.json")));
    }

    @Test
    public void testTakeOrderFailInvalidStatus() throws Exception {
        TakeOrderReq takeOrderReq = new TakeOrderReq();
        takeOrderReq.setStatus("hi");

        mvc.perform(patch("/orders/" + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(takeOrderReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("take-order-fail-invalid-status.json")));
    }

    @Test
    public void testTakeOrderFailInvalidId() throws Exception {
        TakeOrderReq takeOrderReq = new TakeOrderReq();
        takeOrderReq.setStatus(OrderStatus.TAKEN.toString());

        mvc.perform(patch("/orders/" + "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(takeOrderReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(Helpers.loadFile("take-order-fail-invalid-id.json")));
    }

    @Test
    public void testTakeOrderFailOrderAlreadyTaken() throws Exception {
        TakeOrderReq takeOrderReq = new TakeOrderReq();
        takeOrderReq.setStatus(OrderStatus.TAKEN.toString());

        mvc.perform(patch("/orders/" + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(takeOrderReq)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().json(Helpers.loadFile("take-order-fail-order-already-taken.json")));
    }

}
