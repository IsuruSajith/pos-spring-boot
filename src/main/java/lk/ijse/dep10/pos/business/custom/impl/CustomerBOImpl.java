package lk.ijse.dep10.pos.business.custom.impl;

import lk.ijse.dep10.pos.business.custom.CustomerBO;
import lk.ijse.dep10.pos.business.exception.BusinessException;
import lk.ijse.dep10.pos.business.exception.BusinessExceptionType;
import lk.ijse.dep10.pos.business.util.Transformer;
import lk.ijse.dep10.pos.dao.custom.CustomerDAO;
import lk.ijse.dep10.pos.dao.custom.OrderCustomerDAO;
import lk.ijse.dep10.pos.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerBOImpl implements CustomerBO {

    private final Transformer transformer;
    private final CustomerDAO customerDAO;
    private final OrderCustomerDAO orderCustomerDAO;

    public CustomerBOImpl(Transformer transformer, CustomerDAO customerDAO, OrderCustomerDAO orderCustomerDAO) {
        this.transformer = transformer;
        this.customerDAO = customerDAO;
        this.orderCustomerDAO = orderCustomerDAO;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws Exception {
        if (customerDAO.existsCustomerByContact(customerDTO.getContact())) {
            throw new BusinessException(BusinessExceptionType.DUPLICATE_RECORD, "Save failed: Contact number: " + customerDTO.getContact() + " already exists");
        }
        return transformer.fromCustomerEntity(customerDAO.save(transformer.toCustomerEntity(customerDTO)));
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) throws Exception {
        if (customerDAO.existsCustomerByContactAndNotId(customerDTO.getContact(), customerDTO.getId())) {
            throw new BusinessException(BusinessExceptionType.DUPLICATE_RECORD, "Update failed: Contact number: " + customerDTO.getContact() + " already exists");
        }

        if (!customerDAO.existsById(customerDTO.getId()))
            throw new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                    "Update failed: Customer ID: " + customerDTO.getId() + " does not exist");
        customerDAO.update(transformer.toCustomerEntity(customerDTO));
    }

    @Override
    public void deleteCustomerById(int customerId) throws Exception {
        if (orderCustomerDAO.existsOrderByCustomerId(customerId)) {
            throw new BusinessException(BusinessExceptionType.INTEGRITY_VIOLATION, "Delete failed: Customer ID: " + customerId + " is already associated with some orders");
        }

        if (!customerDAO.existsById(customerId))
            throw new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                    "Delete failed: Customer ID: " + customerId + " does not exist");

        customerDAO.deleteById(customerId);
    }

    @Override
    public CustomerDTO findCustomerByIdOrContact(String idOrContact) throws Exception {
        return customerDAO.findCustomerByIdOrContact(idOrContact)
                .map(transformer::fromCustomerEntity)
                .orElseThrow(() -> new BusinessException(BusinessExceptionType.RECORD_NOT_FOUND,
                        "No customer record is associated with the id or contact: " + idOrContact));
    }

    @Override
    public List<CustomerDTO> findCustomers(String query) throws Exception {
        return customerDAO.findCustomers(query).stream()
                .map(transformer::fromCustomerEntity).collect(Collectors.toList());
    }
}
