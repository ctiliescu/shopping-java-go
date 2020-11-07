package com.ctiliescu.shopping.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ctiliescu.shopping.api.AuthApiDelegate;
import com.ctiliescu.shopping.config.service.AuthService;
import com.ctiliescu.shopping.model.Token;
import com.ctiliescu.shopping.model.UserCredentials;
import com.ctiliescu.shopping.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersDelegateImpl implements AuthApiDelegate {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<Token> login(UserCredentials body) {
        Optional<User> userCredentialsOp = userRepository.findByEmail(body.getUsername());
        return userCredentialsOp.filter(user -> user.getPassword().equals(body.getPassword()))
                .map(user -> {
                    Token t = new Token();
                    t.token(authService.generateToken(user.getId()));
                    return new ResponseEntity<Token>(t, HttpStatus.OK);
                }).orElse(new ResponseEntity<Token>(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtLogin = (String) authentication.getCredentials();
        authService.logout(jwtLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
