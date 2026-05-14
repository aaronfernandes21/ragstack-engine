package com.aaron.aiDoc.controller;

import com.aaron.aiDoc.dto.LoginDto;
import com.aaron.aiDoc.entity.User;
import com.aaron.aiDoc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user){
        String response = authService.signup(user);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginDto logindto){
        String response = authService.login(logindto);
        Map<String, String> body = new HashMap<>();
        body.put("accessToken", response);
        return ResponseEntity.ok(body);
    }


}
