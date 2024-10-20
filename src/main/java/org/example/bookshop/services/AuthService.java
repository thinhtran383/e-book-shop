package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.mail.MailDto;
import org.example.bookshop.dto.user.ChangePassDto;
import org.example.bookshop.dto.user.LoginDto;
import org.example.bookshop.dto.user.RegisterDto;
import org.example.bookshop.entities.Customer;
import org.example.bookshop.entities.Role;
import org.example.bookshop.entities.User;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.exceptions.ResourceAlreadyExisted;
import org.example.bookshop.repositories.IUserRepository;
import org.example.bookshop.responses.users.LoginResponse;
import org.example.bookshop.components.JwtGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final MailService mailService;


    @Transactional
    public RegisterDto register(RegisterDto registerDto) {
        User existUser = userRepository.findByUsername(registerDto.getUsername()).orElse(null);

        if (existUser != null) {
            if (existUser.getEmail() != null && existUser.getEmail().equals(registerDto.getEmail())) {
                throw new ResourceAlreadyExisted("Email already existed");
            }

            if (existUser.getCustomer() != null && existUser.getCustomer().getPhone() != null &&
                    existUser.getCustomer().getPhone().equals(registerDto.getPhone())) {
                throw new ResourceAlreadyExisted("Phone number already existed");
            }

            throw new ResourceAlreadyExisted("Username already existed");
        }


        User user = modelMapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        user.setRole(new Role(1, "customer"));

        Customer customer = modelMapper.map(registerDto, Customer.class);
        customer.setUserID(user);

        user.setCustomer(customer);

        userRepository.save(user);

        return registerDto;
    }


    @Transactional(readOnly = true)
    public LoginResponse login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (user.getRole().getId() != 1) {
            throw new BadCredentialsException("You are not have permission to access this page");
        }

        return doLogin(loginDto, user);
    }

    @Transactional(readOnly = true)
    public LoginResponse adminLogin(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()) //
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (user.getRole().getId() != 2) { //
            throw new BadCredentialsException("You are not have permission to access this page");
        }

        return doLogin(loginDto, user);
    }

    private LoginResponse doLogin(LoginDto loginDto, User user) {
        Customer customer = user.getCustomer();

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


    @Transactional
    public LoginResponse changePassword(ChangePassDto changePassDto, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePassDto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!changePassDto.getNewPassword().equals(changePassDto.getConfirmPassword())) {
            throw new BadCredentialsException("Confirm password not match");
        }

        user.setPassword(passwordEncoder.encode(changePassDto.getNewPassword()));


        return LoginResponse.builder()
                .token(jwtGenerator.generateToken(user))
                .phone(user.getCustomer().getPhone())
                .address(user.getCustomer().getAddress())
                .username(user.getUsername())
                .fullName(user.getCustomer().getFullName())
                .email(user.getEmail())
                .build();

    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Email not found"));


        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        MailDto mailDto = MailDto.builder()
                .subject("Forgot password")
                .message(newPassword)
                .to(user.getEmail())
                .build();

        mailService.sendMail(mailDto);
    }

}
