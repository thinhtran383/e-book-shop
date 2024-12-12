package org.example.bookshop.services;

import org.example.bookshop.domain.database.Customer;
import org.example.bookshop.domain.database.Role;
import org.example.bookshop.domain.database.User;
import org.example.bookshop.dto.user.RegisterDto;
import org.example.bookshop.repositories.database.IUserRepository;
import org.example.bookshop.exceptions.ResourceAlreadyExisted;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterWithValidData() {
        // Arrange
        RegisterDto registerDto = RegisterDto.builder()
                .username("validUser")
                .password("ValidPass123")
                .email("valid@example.com")
                .fullName("Valid User")
                .address("123 Main St")
                .phone("0123456789")
                .build();

        User mappedUser = new User();
        Customer customer = new Customer();
        Role role = new Role();
        mappedUser.setCustomer(customer);
        mappedUser.setRole(role);

        Mockito.when(userRepository.findByUsername("validUser")).thenReturn(Optional.empty());
        Mockito.when(modelMapper.map(registerDto, User.class)).thenReturn(mappedUser);
        Mockito.when(passwordEncoder.encode("ValidPass123")).thenReturn("EncodedPassword");

        // Act
        RegisterDto result = authService.register(registerDto);

        // Assert
        assertEquals(registerDto, result);
        Mockito.verify(userRepository).save(mappedUser);
    }

    @Test
    void testRegisterWithExistingUsername() {
        // Arrange
        RegisterDto registerDto = RegisterDto.builder()
                .username("existingUser")
                .password("Password123")
                .email("unique@example.com")
                .fullName("New User")
                .address("456 Another St")
                .phone("0987654321")
                .build();

        User existingUser = new User();
        Mockito.when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExisted.class, () -> authService.register(registerDto));
    }

    @Test
    void testRegisterWithExistingEmail() {
        // Arrange
        RegisterDto registerDto = RegisterDto.builder()
                .username("newUser")
                .password("Password123")
                .email("existing@example.com")
                .fullName("New User")
                .address("1234 Street")
                .phone("0123456789")
                .build();

        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        Mockito.when(userRepository.findByUsername("newUser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExisted.class, () -> authService.register(registerDto));
    }

    @Test
    void testRegisterWithExistingPhoneNumber() {
        // Arrange
        RegisterDto registerDto = RegisterDto.builder()
                .username("newUser")
                .password("Password123")
                .email("new@example.com")
                .fullName("New User")
                .address("1234 Street")
                .phone("0123456789")
                .build();

        User existingUser = new User();
        Customer customer = new Customer();
        customer.setPhone("0123456789");
        existingUser.setCustomer(customer);
        Mockito.when(userRepository.findByUsername("newUser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExisted.class, () -> authService.register(registerDto));
    }
}