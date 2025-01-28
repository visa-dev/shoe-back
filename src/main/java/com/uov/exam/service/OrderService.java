package com.uov.exam.service;

import com.uov.exam.Response.CommonResponse;
import com.uov.exam.dto.OrderRequest;

import com.uov.exam.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    Order createOrder(OrderRequest userRegistrationRequest);

    List<Order> getAllOrdersByUsername(String username);

}
