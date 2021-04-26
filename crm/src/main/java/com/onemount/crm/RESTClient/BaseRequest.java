package com.onemount.crm.RESTClient;

import java.util.List;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Accept: application/json")
interface BaseRequest<V, TDTO> {

  @RequestLine("GET /")
  List<V> list();

  @RequestLine("GET /{id}")
  V get(@Param("id") long key);

  @Headers("Content-Type: application/json")
  @RequestLine("POST /")
  V post(TDTO value);

  @Headers("Content-Type: application/json")
  @RequestLine("PUT /{id}/")
  V put(@Param("id") long id, TDTO value);

  @RequestLine("DELETE /{id}/")
  long delete(@Param("id") long id);
}
