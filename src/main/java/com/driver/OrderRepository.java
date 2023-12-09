package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String,Order> orderMap;
    private HashMap<String,DeliveryPartner> deliveryPartnerMap;
    private HashMap<DeliveryPartner,List<Order>> partnerOrderMap;

    public OrderRepository() {
        orderMap=new HashMap<>();
        deliveryPartnerMap=new HashMap<>();
        partnerOrderMap=new HashMap<>();
    }

    public ResponseEntity<String> addOrder(Order order){
        if(!orderMap.containsKey(order.getId()))
        {
            orderMap.put(order.getId(),order);
        }
        return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addPartner(String partnerId){
        if(!deliveryPartnerMap.containsKey(partnerId))
        {
            DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
            deliveryPartnerMap.put(partnerId,deliveryPartner);
        }
        return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addOrderPartnerPair(String orderId, String partnerId){
        Order order=orderMap.get(orderId);
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        partnerOrderMap.putIfAbsent(deliveryPartner,new ArrayList<Order>());
        List<Order> orders=partnerOrderMap.get(deliveryPartner);
        orders.add(order);
        partnerOrderMap.put(deliveryPartner,orders);
        //This is basically assigning that order to that partnerId
        return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<Order> getOrderById(String orderId){
        Order order= orderMap.get(orderId);
        //order should be returned with an orderId.

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    public ResponseEntity<DeliveryPartner> getPartnerById(String partnerId){
        DeliveryPartner deliveryPartner = deliveryPartnerMap.get(partnerId);
        //deliveryPartner should contain the value given by partnerId

        return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> getOrderCountByPartnerId(String partnerId){
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        Integer orderCount = partnerOrderMap.get(deliveryPartner).size();
        //orderCount should denote the orders given by a partner-id

        return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
    }

    public ResponseEntity<List<String>> getOrdersByPartnerId(String partnerId){
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        List<String> orders = new ArrayList<>();
        for(Order order:partnerOrderMap.get(deliveryPartner))
        {
            orders.add(order.getId());
        }
        //orders should contain a list of orders by PartnerId

        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    public ResponseEntity<List<String>> getAllOrders(){
        List<String> orders = new ArrayList<>();
        orders.addAll(orderMap.keySet());
        //Get all orders
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        Integer countOfOrders = 0;
//        int totalAssignedOrders=0;
//        for(DeliveryPartner deliveryPartner:partnerOrderMap.keySet())
//        {
//            totalAssignedOrders+=partnerOrderMap.get(deliveryPartner).size();
//        }
//        int totalOrders=orderMap.size();
//        countOfOrders=totalOrders-totalAssignedOrders;
        for(String orderId:orderMap.keySet())
        {
            boolean isOrderAssigned=false;
            for(DeliveryPartner deliveryPartner:partnerOrderMap.keySet())
            {
                if(partnerOrderMap.get(deliveryPartner).contains(orderMap.get(orderId)))
                {
                    isOrderAssigned=true;
                }
            }
            if(!isOrderAssigned)
            {
                countOfOrders++;
            }
        }
        //Count of orders that have not been assigned to any DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        Integer countOfOrders = 0;
        int convertedTime=(Integer.parseInt(time.substring(0,2))*60)+Integer.parseInt(time.substring(3));
        for(Order order:partnerOrderMap.get(deliveryPartner))
        {
            if(order.getDeliveryTime()>convertedTime)
            {
                countOfOrders++;
            }
        }
        //countOfOrders that are left after a particular time of a DeliveryPartner

        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(String partnerId){
        String time = null;
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        int maxTime=0;
        for(Order order:partnerOrderMap.get(deliveryPartner))
        {
            int deliveryTime=order.getDeliveryTime();
            if(deliveryTime>maxTime)
            {
                time=convertTime(deliveryTime);
                maxTime=deliveryTime;
            }
        }
        //Return the time when that partnerId will deliver his last delivery order.

        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        partnerOrderMap.remove(deliveryPartner);
        deliveryPartnerMap.remove(partnerId);
        return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        Order order=orderMap.get(orderId);
        for(DeliveryPartner deliveryPartner:partnerOrderMap.keySet())
        {
            if(partnerOrderMap.get(deliveryPartner).contains(order))
            {
                partnerOrderMap.get(deliveryPartner).remove(order);
            }
        }
        orderMap.remove(orderId);
        return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.CREATED);
    }
    public String convertTime(int deliveryTime)
    {
        String hrs="";
        String mns="";
        int hours=deliveryTime/60;
        int mins=deliveryTime%60;
        if(hours<10)
        {
            hrs="0"+hours;
        }
        else {
            hrs=Integer.toString(hours);
        }

        if(mins<10)
        {
            mns="0"+mins;
        }
        else {
            mns=Integer.toString(mins);
        }
        return hrs+":"+mns;
    }
}
