package com.ctiliescu.shopping.users;

import com.ctiliescu.shopping.api.UsersApiDelegate;
import com.ctiliescu.shopping.model.Token;
import com.ctiliescu.shopping.model.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersDelegateImpl implements UsersApiDelegate {

    @Override
    public ResponseEntity<Token> login(UserCredentials body) {
        Token t = new Token();
        t.setToken("asdasd");
        return new ResponseEntity<Token>(t, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
