package com.ctiliescu.shopping.carts.model;

import com.ctiliescu.shopping.carts.service.ProductRepository;
import com.ctiliescu.shopping.model.CartElement;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartsService {
    private static Map<Integer, Map<Integer, Integer>> inMemoryCart =  new HashMap<>();
    @Autowired
    private ProductRepository productRepository;

    public void addInCart(CartElement element, int userId) {
        Map<Integer, Integer> map = inMemoryCart.getOrDefault(userId, new HashMap<>());
        int stock = map.getOrDefault(element.getProductId(), 0);
        map.put(element.getProductId(), stock + element.getQuantity());
        inMemoryCart.put(userId, map);
    }

    @Transactional
    public boolean order(int userId) {
        Map<Integer, Integer> cart = inMemoryCart.getOrDefault(userId, new HashMap<>());
        List<Product> productsToBeUpdated = cart.keySet().stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(e -> e.getStock() > cart.get(e.getId()))
                .collect(Collectors.toList());
        if(productsToBeUpdated.size() == cart.size()) {
            productsToBeUpdated.forEach(e -> productRepository.setProductStock(e.getStock() - cart.get(e.getId()), e.getId()));
                return true;
        }

        return false;
    }
}
