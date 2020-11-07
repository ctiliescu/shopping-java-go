package com.ctiliescu.shopping.carts;

import com.ctiliescu.shopping.api.CartsApiDelegate;
import com.ctiliescu.shopping.carts.service.CartsService;
import com.ctiliescu.shopping.carts.service.PaymentService;
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

    @Autowired
    private PaymentService paymentService;

    public ResponseEntity<Void> addToCarts(CartElement body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        cartsService.addInCart(body, (int) authentication.getPrincipal());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> order() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = (int) authentication.getPrincipal();
        Boolean response = cartsService.order(userId);

        if (response) {
            if(paymentService.checkPayment()) {
                cartsService.finishOrder(userId);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                cartsService.revertOrder(userId);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
