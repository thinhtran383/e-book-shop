package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.LoginDto;
import org.example.bookshop.dto.RegisterDto;
import org.example.bookshop.entities.Customer;
import org.example.bookshop.entities.Role;
import org.example.bookshop.entities.User;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.ICustomerRepository;
import org.example.bookshop.repositories.IRoleRepository;
import org.example.bookshop.repositories.IUserRepository;
import org.example.bookshop.responses.users.LoginResponse;
import org.example.bookshop.utils.JwtGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ICustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;


    @Transactional
    public RegisterDto register(RegisterDto registerDto) {
        User user = modelMapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        Customer customer = modelMapper.map(registerDto, Customer.class);
        customer.setUserID(user);

        user.getCustomers().add(customer);

        userRepository.save(user);

        return registerDto;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return doLogin(loginDto, user);
    }

    @Transactional(readOnly = true)
    public LoginResponse adminLogin(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if(user.getRole().getId() != 2) {
            throw new BadCredentialsException("You are not have permission to access this page");
        }

        return doLogin(loginDto, user);
    }

    private LoginResponse doLogin(LoginDto loginDto, User user) {
        Customer customer = customerRepository.findByUserID(user)
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword(),
                user.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        String token = jwtGenerator.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(customer.getFullName())
                .email(user.getEmail())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .build();
    }


}
