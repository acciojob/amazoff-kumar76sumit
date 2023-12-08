package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository=new OrderRepository();

    public ResponseEntity<String> addOrder(Order order){
        return orderRepository.addOrder(order);
    }

    public ResponseEntity<String> addPartner(String partnerId){
        return orderRepository.addPartner(partnerId);
    }

    public ResponseEntity<String> addOrderPartnerPair(String orderId, String partnerId){

        //This is basically assigning that order to that partnerId
        return orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public ResponseEntity<Order> getOrderById(String orderId){
        //order should be returned with an orderId.

        return orderRepository.getOrderById(orderId);
    }

    public ResponseEntity<DeliveryPartner> getPartnerById(String partnerId){
        //deliveryPartner should contain the value given by partnerId

        return orderRepository.getPartnerById(partnerId);
    }

    public ResponseEntity<Integer> getOrderCountByPartnerId(String partnerId){
        //orderCount should denote the orders given by a partner-id

        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public ResponseEntity<List<String>> getOrdersByPartnerId(String partnerId){
        //orders should contain a list of orders by PartnerId

        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public ResponseEntity<List<String>> getAllOrders(){
        //Get all orders
        return orderRepository.getAllOrders();
    }

    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        //Count of orders that have not been assigned to any DeliveryPartner

        return orderRepository.getCountOfUnassignedOrders();
    }

    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        //countOfOrders that are left after a particular time of a DeliveryPartner

        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }

    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(String partnerId){
        //Return the time when that partnerId will deliver his last delivery order.

        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public ResponseEntity<String> deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        return orderRepository.deletePartnerById(partnerId);
    }

    public ResponseEntity<String> deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId

        return orderRepository.deleteOrderById(orderId);
    }
}
