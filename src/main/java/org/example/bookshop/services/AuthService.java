package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.RegisterDto;
import org.example.bookshop.entities.Customer;
import org.example.bookshop.entities.Role;
import org.example.bookshop.entities.User;
import org.example.bookshop.repositories.ICustomerRepository;
import org.example.bookshop.repositories.IRoleRepository;
import org.example.bookshop.repositories.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ICustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


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


}
