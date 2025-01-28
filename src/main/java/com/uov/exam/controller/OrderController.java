package com.uov.exam.controller;

import com.uov.exam.dto.OrderRequest;
import com.uov.exam.model.Order;
import com.uov.exam.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/auth/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> creteOrder(@RequestBody OrderRequest order) {

        Order newOrder= orderService.createOrder(order);
        if(newOrder!=null){
            return new ResponseEntity<>(newOrder, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/summery")
    public ResponseEntity<?> getAllOrders(@RequestBody OrderRequest order) {
        System.out.println(order.getUsername());
        List<Order> allOrders = orderService.getAllOrdersByUsername(order.getUsername());
        System.out.println(allOrders);
        if(allOrders!=null){
            return new ResponseEntity<>(allOrders, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
