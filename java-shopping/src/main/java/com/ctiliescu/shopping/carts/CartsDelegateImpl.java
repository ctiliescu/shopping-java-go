package com.ctiliescu.shopping.carts;

import com.ctiliescu.shopping.api.CartsApiDelegate;
import com.ctiliescu.shopping.carts.model.CartsService;
import com.ctiliescu.shopping.model.CartElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartsDelegateImpl implements CartsApiDelegate {
    @Autowired
    private CartsService cartsService;

    public ResponseEntity<Void> addToCarts(CartElement body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cartsService.addInCart(body, (int) authentication.getPrincipal());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> order() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean response = cartsService.order((int) authentication.getPrincipal());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
