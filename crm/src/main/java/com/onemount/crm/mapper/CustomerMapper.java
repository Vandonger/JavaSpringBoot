package com.onemount.crm.mapper;

import com.onemount.crm.dto.CustomerDTO;
import com.onemount.crm.model.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    CustomerDTO customerToDTO(Customer customer);
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
