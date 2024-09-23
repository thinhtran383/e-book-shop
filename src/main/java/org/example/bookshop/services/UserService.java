package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.RegisterDto;
import org.example.bookshop.entities.Customer;
import org.example.bookshop.entities.Role;
import org.example.bookshop.entities.User;
import org.example.bookshop.repositories.IUserRepository;
import org.example.bookshop.responses.users.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userRepository.findAllUserDetails(pageable);

        return users.map(user -> modelMapper.map(user, UserResponse.class));
    }




}
