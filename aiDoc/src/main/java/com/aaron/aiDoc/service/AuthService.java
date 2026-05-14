package com.aaron.aiDoc.service;

import com.aaron.aiDoc.dto.LoginDto;
import com.aaron.aiDoc.entity.Role;
import com.aaron.aiDoc.entity.User;
import com.aaron.aiDoc.repository.UserRepo;
import com.aaron.aiDoc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;

    public String signup(User user) {
        Optional<User> user1 = userRepo.findByEmail((user.getEmail()));
        if(user1.isPresent()){
            return "User already exists";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepo.save(user);
        return "Register Success";

    }

    public String login(LoginDto logindto) {
        Optional<User> user = userRepo.findByEmail(logindto.getEmail());

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }
        if(!passwordEncoder.matches(logindto.getPassword(),user.get().getPassword())){
            //throw new PasswordNotMatchException(logindto.getPassword());
            return  "Wrong Password";
        }
        UUID userId = user.get().getId();
        Role role = user.get().getRole();
        String token = jwtUtil.generateToken(user.get().getUsername(), user.get().getEmail(), userId, role);
        return token;
    }
}
