package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Food;
import com.ttcs.ttcs.enity.FoodOrder;
import com.ttcs.ttcs.enity.OrderItem;
import com.ttcs.ttcs.repository.FoodOrderRepository;
import com.ttcs.ttcs.repository.FoodRepository;
import com.ttcs.ttcs.repository.OrderItemRepository;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository itemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final FoodRepository foodRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public OrderItemService(OrderItemRepository itemRepository,
                            FoodOrderRepository foodOrderRepository,
                            FoodRepository foodRepository,
                            SimpMessagingTemplate messagingTemplate) {
        this.itemRepository = itemRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.foodRepository = foodRepository;
        this.messagingTemplate=messagingTemplate;
    }

    // all items theo order
    public List<OrderItem> getItemsByOrderId(Long id){
        return itemRepository.findByFoodOrderId(id);
    }
    // item theo id
    public OrderItem getById(Long id){
        return itemRepository.findById(id).orElse(null);
    }
    // save

    public void save(OrderItem orderItem){

        itemRepository.save(orderItem);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    // add
    public void addItem(Long orderId, Long foodId, Integer quantity){
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
        Food food = foodRepository.findById(foodId).orElse(null);
        OrderItem orderItem = itemRepository.findByFoodOrderIdAndFoodIdAndCookingFinishedAtIsNull(orderId, foodId).orElse(null);

        if(orderItem != null){
            orderItem.setQuantity(orderItem.getQuantity()+quantity);
        }
        else {
            orderItem = new OrderItem();
            orderItem.setFoodOrder(foodOrder);
            orderItem.setFood(food);
            orderItem.setQuantity(quantity);
            orderItem.setOrderedAt(LocalDateTime.now());
        }
        itemRepository.save(orderItem);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    public void deleteItem(Long itemId){
        itemRepository.deleteById(itemId);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    public void startCooking(Long itemId){
        OrderItem item = getById(itemId);
        item.startCooking();
        itemRepository.save(item);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    public void finishCooking(Long itemId){
        OrderItem item = getById(itemId);
        item.finishCooking();
        itemRepository.save(item);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    public void serve(Long itemId){
        OrderItem item = getById(itemId);
        item.serve();
        itemRepository.save(item);
        messagingTemplate.convertAndSend("/topic/waiting", "update");
    }
    public Long total(Long orderId){
        List<OrderItem> items = itemRepository.findByFoodOrderId(orderId);
        Long total = 0L;
        for(OrderItem item: items){
            total+=item.getSubTotal();
        }
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElseThrow();
        foodOrder.setTotalAmount(total);
        foodOrderRepository.save(foodOrder);
        return total;
    }
//    ds chờ hôm nay
    public List<OrderItem> waitingFood(){
        return itemRepository.findOrderItemByDate(LocalDate.now());
    }
}
