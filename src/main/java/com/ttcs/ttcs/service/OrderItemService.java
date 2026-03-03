package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Food;
import com.ttcs.ttcs.enity.FoodOrder;
import com.ttcs.ttcs.enity.OrderItem;
import com.ttcs.ttcs.repository.FoodOrderRepository;
import com.ttcs.ttcs.repository.FoodRepository;
import com.ttcs.ttcs.repository.OrderItemRepository;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository itemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final FoodRepository foodRepository;

    public OrderItemService(OrderItemRepository itemRepository, FoodOrderRepository foodOrderRepository, FoodRepository foodRepository) {
        this.itemRepository = itemRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.foodRepository = foodRepository;
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
    public OrderItem save(OrderItem orderItem){
        return itemRepository.save(orderItem);
    }
    // add
    public void addItem(Long orderId, Long foodId, Integer quantity){
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
        Food food = foodRepository.findById(foodId).orElse(null);
        OrderItem orderItem = itemRepository.findByFoodOrderIdAndFoodId(orderId, foodId).orElse(null);

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
    }
    public void deleteItem(Long itemId){
        itemRepository.deleteById(itemId);
    }
    public void startCooking(Long itemId){
        OrderItem item = getById(itemId);
        item.startCooking();
    }
    public void finishCooking(Long itemId){
        OrderItem item = getById(itemId);
        item.finishCooking();
    }
    public void serve(Long itemId){
        OrderItem item = getById(itemId);
        item.serve();
    }
    public Long total(Long orderId){
        List<OrderItem> items = itemRepository.findByFoodOrderId(orderId);
        Long total = 0L;
        for(OrderItem item: items){
            total+=item.getSubTotal();
        }
        return total;
    }
}
