package com.onemount.crm.RESTClient;
import java.util.List;
import com.onemount.crm.dto.CustomerDTO;
import com.onemount.crm.model.Customer;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
public interface CustomerRequest extends BaseRequest<Customer, CustomerDTO>{
  
  @Headers("Content-Type: application/json")
  @RequestLine("PATCH /{id}/")
  @Body("{title}")
  String patch(@Param("id") long id, @Param("title") String title);


  @RequestLine("GET /slow")
  List<Customer> listSlow();
}