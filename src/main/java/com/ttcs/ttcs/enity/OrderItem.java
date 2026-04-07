package com.ttcs.ttcs.enity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
//    uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "food_id"}))
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private FoodOrder foodOrder;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private Integer quantity;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "cooking_started_at")
    private LocalDateTime cookingStartedAt;

    @Column(name = "cooking_finished_at")
    private LocalDateTime cookingFinishedAt;

    @Column(name = "served_at")
    private LocalDateTime servedAt;

    @Transient
    public String getStatus(){
        if(servedAt != null) return "served";
        if(cookingFinishedAt != null) return "ready";
        if(cookingStartedAt != null) return "preparing";
        return "ordered";
    }
    @Transient
    public Long getSubTotal(){
        return food.getPrice() * quantity;
    }
    public void startCooking(){
        this.cookingStartedAt = LocalDateTime.now();
    }
    public void finishCooking(){
        this.cookingFinishedAt = LocalDateTime.now();
    }
    public void serve(){
        this.servedAt = LocalDateTime.now();
    }

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public LocalDateTime getCookingStartedAt() {
        return cookingStartedAt;
    }

    public void setCookingStartedAt(LocalDateTime cookingStartedAt) {
        this.cookingStartedAt = cookingStartedAt;
    }

    public LocalDateTime getCookingFinishedAt() {
        return cookingFinishedAt;
    }

    public void setCookingFinishedAt(LocalDateTime cookingFinishedAt) {
        this.cookingFinishedAt = cookingFinishedAt;
    }

    public LocalDateTime getServedAt() {
        return servedAt;
    }

    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }
}
