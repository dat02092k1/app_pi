package com.project.shopapp.components;

import com.project.shopapp.models.Category;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@KafkaListener(id = "groupA", topics = {"insert-a-category", "get-all-categories"})
public class MyKafkaListener {

    @KafkaHandler
    public void listenCategory(Category category) {
        System.out.println("Received category: " + category);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Received unknown: " + object);
    }

    @KafkaHandler
    public void listenListCategory(List<Category> categories) {
        System.out.println("Received list of categories: " + categories);
    }
}