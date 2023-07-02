package lk.ijse.dep10.pos.api;

import lk.ijse.dep10.pos.business.custom.OrderBO;
import lk.ijse.dep10.pos.dto.OrderDTO;
import lk.ijse.dep10.pos.dto.OrderDTO2;
import lk.ijse.dep10.pos.dto.util.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/orders")
@RestController
@CrossOrigin
public class OrderController {

    private final OrderBO orderBO;

    public OrderController(OrderBO orderBO) {
        this.orderBO = orderBO;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Integer saveOrder(@RequestBody @Validated(ValidationGroups.Save.class) OrderDTO order) throws Exception {
        return orderBO.placeOrder(order);
    }

    @GetMapping
    public List<OrderDTO2> getOrders(@RequestParam(value = "q", required = false) String query) throws Exception {
        if (query == null) query = "";
        return orderBO.searchOrders(query);
    }
}
