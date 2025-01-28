package com.uov.exam.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "order")
@Data
public class Order {

    @Id
    private String id;

    private String orderId;

    private String username;

    private List<OrderItem> orderItemList;

    private double totalPrice;

    private String status;
}
