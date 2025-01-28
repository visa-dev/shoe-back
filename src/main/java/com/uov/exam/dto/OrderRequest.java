package com.uov.exam.dto;

import com.uov.exam.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {


    private String orderId;

    private String username;

    private List<OrderItem> orderItemList;

    private double totalPrice;

    private String status;
}
