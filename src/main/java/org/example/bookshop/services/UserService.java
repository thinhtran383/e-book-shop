package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.user.UpdateUserDto;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.repositories.database.IUserRepository;
import org.example.bookshop.responses.users.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#user")
    public User getUserByUsername(String user) {
        return userRepository.findByUsername(user).orElseThrow(() -> new RuntimeException("User not found: " + user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetailsById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .address(user.getCustomer().getAddress())
                .email(user.getEmail())
                .fullName(user.getCustomer().getFullName())
                .phoneNumber(user.getCustomer().getPhone())
                .createdDate(user.getCustomer().getCreatedDate())
                .build();
    }

    @Transactional
    public UserResponse updateUserDetails(Integer userId, UpdateUserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getFullName() != null) {
            user.getCustomer().setFullName(userDto.getFullName());
        }

        if (userDto.getAddress() != null) {
            user.getCustomer().setAddress(userDto.getAddress());
        }

        if (userDto.getPhone() != null) {
            user.getCustomer().setPhone(userDto.getPhone());
        }


        userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .address(user.getCustomer().getAddress())
                .email(user.getEmail())
                .fullName(user.getCustomer().getFullName())
                .phoneNumber(user.getCustomer().getPhone())
                .createdDate(user.getCustomer().getCreatedDate())
                .build();
    }


}
