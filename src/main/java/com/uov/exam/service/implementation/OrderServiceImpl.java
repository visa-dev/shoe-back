package com.uov.exam.service.implementation;

import com.uov.exam.Response.CommonResponse;
import com.uov.exam.dto.OrderRequest;
import com.uov.exam.model.Order;
import com.uov.exam.model.OrderItem;
import com.uov.exam.repo.OrderRepo;
import com.uov.exam.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepo orderRepo;


    @Override
    public Order createOrder(OrderRequest order) {
        System.out.println(order.getStatus());
        Order neworder= new Order();
        neworder.setOrderItemList(order.getOrderItemList());
        neworder.setOrderId(order.getOrderId());
        neworder.setUsername(order.getUsername());
        neworder.setTotalPrice(order.getTotalPrice());

        for (OrderItem orderItem : order.getOrderItemList()) {
            orderItem.setStatus(order.getStatus());
        }

        orderRepo.save(neworder);

        return neworder;
    }

    @Override
    public List<Order> getAllOrdersByUsername(String username) {
        return orderRepo.findByUsername(username);
    }
}

