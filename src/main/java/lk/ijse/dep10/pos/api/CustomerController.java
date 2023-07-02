package lk.ijse.dep10.pos.api;

import io.swagger.annotations.*;
import lk.ijse.dep10.pos.business.custom.CustomerBO;
import lk.ijse.dep10.pos.dto.CustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin
@Api("Customer Controller REST API")
public class CustomerController {

    private final CustomerBO customerBO;

    public CustomerController(CustomerBO customerBO) {
        this.customerBO = customerBO;
    }

    @ApiOperation(value = "Save Customer",
            notes = "Save a customer with JSON request body")
    @ApiResponses({
            @ApiResponse(code = 201, message = "New Customer has been created"),
            @ApiResponse(code = 400, message = "Customer details are invalid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody @Valid
                                    @ApiParam(name = "customer", value = "Customer JSON")
                                    CustomerDTO customer) throws Exception {
        return customerBO.saveCustomer(customer);
    }

    @ApiOperation(value = "Update Customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId,
                               @RequestBody @Valid CustomerDTO customer) throws Exception {
        customer.setId(customerId);
        customerBO.updateCustomer(customer);
    }

    @ApiOperation(value = "Delete Customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer customerId) throws Exception {
        customerBO.deleteCustomerById(customerId);
    }

    @ApiOperation(value = "Get Customers")
    @GetMapping
    public List<CustomerDTO> getCustomers(@RequestParam(value = "q", required = false)
                                          String query) throws Exception {
        if (query == null) query = "";
        return customerBO.findCustomers(query);
    }
}
