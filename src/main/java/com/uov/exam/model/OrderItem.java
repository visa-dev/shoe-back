package com.uov.exam.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "order_item")
@Data
public class OrderItem {
    @Id
    private String id;

    private String name;

    private String description;

    private int quantity;

    private int price;

    private String status;
}
