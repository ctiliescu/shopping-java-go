package com.ctiliescu.shopping.users;

import com.ctiliescu.shopping.api.AuthApiDelegate;
import com.ctiliescu.shopping.model.Token;
import com.ctiliescu.shopping.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersDelegateImpl implements AuthApiDelegate {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Token> login(UserCredentials body) {
        Optional<User> userCredentialsOp = userRepository.findByName(body.getUsername());
        return userCredentialsOp.filter(user -> user.getPassword().equals(body.getPassword()))
                .map(user -> {
                    Token t = new Token();
                    t.setToken("asdasd");
                    return new ResponseEntity<Token>(t, HttpStatus.OK);
                }).orElse(new ResponseEntity<Token>(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public ResponseEntity<Void> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
