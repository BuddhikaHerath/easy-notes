package com.example.easynotes.controller;

import com.example.easynotes.dto.OrderProductDTO;
import com.example.easynotes.dto.OrdersDTO;
import com.example.easynotes.dto.ProductDTO;
import com.example.easynotes.model.*;
import com.example.easynotes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CartController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductsRepository orderProductsRepository;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    UserRepository userRepository;

    @Autowired
    ProductRepository prodRepository;

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @PostMapping("/cart")
    public OrderProducts createCart(@Valid @RequestBody Orders newOrder){
        Optional<User> userDetails = userRepository.findByUsername(newOrder.getUser().getUsername());
        Orders order = orderRepository.findByUserIdAndOrderStatus(userDetails.get().getId(), "cart");

        if(order==null){
            order = new Orders();
            order.setOrderStatus("cart");
            order.setUser(userDetails.get());
            order = orderRepository.save(order);
        }
        OrderProducts newOrderProd = newOrder.getOrderProducts().get(0);
        Product product = prodRepository.findById(newOrderProd.getProduct().getId()).get();
        List<OrderProducts> orderProdList = orderProductsRepository.findByOrdersAndProduct(order,product);

        OrderProducts orderProd=null;
        if(orderProdList.size()==0) {
            newOrderProd.setOrders(orderRepository.findById(order.getId()).get());
            orderProd = newOrderProd;
        }else{
            orderProd = orderProdList.get(0);
            orderProd.setQuantity(orderProd.getQuantity()+newOrderProd.getQuantity());
        }
        orderProd = orderProductsRepository.save(orderProd);
        return orderProd;
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @PostMapping("/getcart")
    public List<OrderProductDTO> getCart(@RequestBody User user) {
//        System.err.println(user.getUsername());
//        System.err.println(user.getName());
        //long userId = 2;
        Optional<User> userDetails = userRepository.findByUsername(user.getUsername());
        Orders order = orderRepository.findByUserIdAndOrderStatus(userDetails.get().getId(), "cart");
        //OrdersDTO orderDTO = ordersToDTO(order);
        List<OrderProductDTO> orderProdDTOList = new ArrayList<>();
        if(order!=null){
            List<OrderProducts> orderProdList = orderProductsRepository.findByOrders(order);
            for(OrderProducts orderProd : orderProdList){
                Product product = orderProd.getProduct();
                if(product!=null){
                    ProductDTO productDTO = productToDTO(product);
                    OrderProductDTO orderProductDTO = orderProductToDTO(orderProd);

                    OrdersDTO ordersDTO = new OrdersDTO();
                    ordersDTO.setId(order.getId());

                    orderProductDTO.setProductDTO(productDTO);
                    orderProductDTO.setOrdersDTO(ordersDTO);
                    orderProdDTOList.add(orderProductDTO);
                }
            }
        }
        return orderProdDTOList;
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @PutMapping("/cart/{id}")
    public ResponseEntity<Object> updateCart(@PathVariable(value = "id")Long cartID,
                                             @Valid @RequestBody OrderProducts newOrderProduct){
        Product product = prodRepository.findById(newOrderProduct.getProduct().getId()).get();
        Orders orders = orderRepository.findById(cartID).get();
         List<OrderProducts> orderProds = orderProductsRepository.
                 findByOrdersAndProduct(orders,product);

       // System.out.println("cart id "+cartID);
       // System.err.println(orderProds.size());
        if(orderProds.size()>0 ){
            OrderProducts orderProduct = orderProds.get(0);
            //System.err.println(product.getQuantity());
            if(newOrderProduct.getQuantity()>0 && newOrderProduct.getQuantity()<=product.getQuantity()) {
                orderProduct.setQuantity(newOrderProduct.getQuantity());
                //System.out.println("Quantity : " + newOrderProduct.getQuantity());
                orderProductsRepository.save(orderProduct);
                OrderProductDTO orderProductDTO = orderProductToDTO(orderProduct);
                return new ResponseEntity<>(orderProductDTO, HttpStatus.OK);
            }
           //return orderProduct;

        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        return null;
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @GetMapping("/cart/{id}/{status}")
    public Orders updateCartStatus(@PathVariable(value = "id")Long cartID, @PathVariable(value = "status")String status){
        Optional<Orders> orders = orderRepository.findById(cartID);
        if(orders.isPresent()){
            Orders order = orders.get();
            order.setOrderStatus(status);
            orderRepository.save(order);

            List<OrderProducts> orderProds = orders.get().getOrderProducts();
            for(OrderProducts orderProd : orderProds){
                Product prod = orderProd.getProduct();
                prod.setQuantity(prod.getQuantity()-orderProd.getQuantity());
                prodRepository.save(prod);
            }
            order.setOrderProducts(null);
            return order;
        }
        return null;
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @DeleteMapping("/cart/{cartId}/{productId}")
    public boolean deleteCartProduct(@PathVariable Long cartId
    ,@PathVariable Long productId) {
        Product product = prodRepository.findById(productId).get();
        Orders order = orderRepository.findById(cartId).get();
        List<OrderProducts> orderProds = orderProductsRepository.
                findByOrdersAndProduct(order,product);
        if(orderProds.size()>0){
            OrderProducts orderProduct = orderProds.get(0);
            orderProductsRepository.delete(orderProduct);
            return true;
        }
        return false;
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
    @DeleteMapping("/cart/{id}")
    public boolean deleteCart(@PathVariable(value = "id") Long cartID) {
        Optional<Orders> order = orderRepository.findById(cartID);
        if(order.isPresent()){
            List<OrderProducts> orderProds = orderProductsRepository.findByOrders(order.get());
            for(OrderProducts orderProd : orderProds){
                orderProductsRepository.deleteById(orderProd.getId());
            }
            orderRepository.deleteById(cartID);
            return true;
        }
        return false;
    }

    private ProductDTO productToDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCompany(product.getCompany());
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setImage(product.getImage());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setTitle(product.getTitle());
        return productDTO;
    }

    private OrderProductDTO orderProductToDTO(OrderProducts orderProduct){
        OrderProductDTO orderProductDTO = new OrderProductDTO();
        orderProductDTO.setId(orderProduct.getId());
        orderProductDTO.setQuantity(orderProduct.getQuantity());
        return orderProductDTO;
    }

}
