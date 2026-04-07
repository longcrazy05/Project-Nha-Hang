package com.ttcs.ttcs.service;

import com.ttcs.ttcs.dto.CartItem;
import com.ttcs.ttcs.enity.*;
import com.ttcs.ttcs.repository.CustomerRepository;
import com.ttcs.ttcs.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final FoodService foodService;
    private final OrderItemService orderItemService;
    private final ReservationService reservationService;
    private final CustomerRepository customerRepository;
    private final FoodOrderService foodOrderService;
    private final RestaurantTableService tableService;
    private final EmployeeRepository employeeRepository;

    public CustomerService(FoodService foodService,
                           OrderItemService orderItemService,
                           ReservationService reservationService,
                           CustomerRepository customerRepository,
                           FoodOrderService foodOrderService,
                           RestaurantTableService tableService,
                           EmployeeRepository employeeRepository) {
        this.foodService = foodService;
        this.orderItemService = orderItemService;
        this.reservationService = reservationService;
        this.customerRepository = customerRepository;
        this.foodOrderService = foodOrderService;
        this.tableService = tableService;
        this.employeeRepository = employeeRepository;
    }

    // find by id
    public Customer findById(Long id){
        return customerRepository.findById(id).orElse(null);
    }
    // save
    public void save(Customer customer){
        customerRepository.save(customer);
    }
    // ds order
    public List<FoodOrder> foodOrderList(Long id){
        return foodOrderService.customerOrderList(id);
    }
    // cac mon trong order
    public List<OrderItem> orderItemList(Long id){
        return orderItemService.getItemsByOrderId(id);
    }

    // ds reservation
    public List<Reservation> reservationList(Long id){
        return reservationService.CustomerReservationList(id);
    }
    // ds mon
    public List<Food> foodList(){
        return foodService.findAll();
    }
    // ds ban trong
    public List<RestaurantTable> availableTable(){
        return tableService.availableTables();
    }
    // tổng chi tiêu
    public Long totalSpent(Long id){
        return foodOrderService.totalSpent(id);
    }
    //
    public List<Long> weekValues(Long customerId){

        List<Object[]> raw =
                foodOrderService.weekStats(customerId);

        List<Long> values = new ArrayList<>();

        for(Object[] r : raw){
            values.add(((Number) r[1]).longValue());
        }

        return values;
    }
    public List<String> weekLabels(Long customerId){

        List<Object[]> raw =
                foodOrderService.weekStats(customerId);

        List<String> labels = new ArrayList<>();

        for(Object[] r : raw){
            labels.add((String) r[0]);
        }

        return labels;
    }

    public Customer login(String username,String password){

        return customerRepository
                .findByUsernameAndPassword(username,password)
                .orElse(null);

    }
    public boolean existsUsername(String username){
        return customerRepository.existsByUsername(username)
                || employeeRepository.existsByUsername(username);
    }

    public boolean existsEmail(String email){
        return customerRepository.existsByEmail(email)
                || employeeRepository.existsByEmail(email);
    }

    public boolean existsPhone(String phone){
        return customerRepository.existsByPhone(phone)
                || employeeRepository.existsByPhone(phone);
    }

    @Transactional
    public void createFromCart(Long customerId, List<CartItem> cart){

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();

        // tạo order
        FoodOrder order = new FoodOrder();
        order.setCustomer(customer);
        order.setOpenedAt(LocalDateTime.now());
        order.setOrderType("ONLINE");
        order.setTotalAmount(0L);

        foodOrderService.save(order);

        long total = 0;

        for(CartItem c : cart){

            Food food = foodService.findById(c.getId());

            OrderItem item = new OrderItem();

            item.setFoodOrder(order); // đúng field của bạn
            item.setFood(food);
            item.setQuantity(c.getQuantity());

            item.setOrderedAt(LocalDateTime.now()); // thêm thời gian

            orderItemService.save(item);

            total += food.getPrice() * c.getQuantity(); // lấy từ food
        }

        order.setTotalAmount(total);

        foodOrderService.save(order);
    }
}
