package com.jdum.commerce.sumysoul.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jdum.commerce.sumysoul.configuration.mapper.UserMapper;
import com.jdum.commerce.sumysoul.domain.Authority;
import com.jdum.commerce.sumysoul.domain.User;
import com.jdum.commerce.sumysoul.dto.UserDto;
import com.jdum.commerce.sumysoul.repository.UserRepository;
import com.jdum.commerce.sumysoul.web.error.NotFoundException;
import com.jdum.commerce.sumysoul.web.error.UnauthorizedException;
import com.jdum.commerce.sumysoul.web.error.UserExistsException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository repository;
  @Mock
  private UserMapper mapper;
  @Mock
  private ApplicationContext context;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldLoadUserByUserName() {
    var username = "testUser";
    User user = User.builder().login(username).password("password").build();
    user.setLogin(username);
    user.setPassword("password");
    user.setAuthorities(EnumSet.of(Authority.USER));

    when(repository.findOneByLogin(username)).thenReturn(Optional.of(user));

    var userDetails = userService.loadUserByUsername(username);
    assertEquals(username, userDetails.getUsername());
  }

  @Test
  void shouldThrowUserNotFoundWhenLoadNonExistingUser() {
    var username = "unknownUser";
    when(repository.findOneByLogin(username)).thenReturn(Optional.empty());

    assertThrows(UnauthorizedException.class, () -> userService.loadUserByUsername(username));
  }

  @Test
  void shouldLoadAllUsers() {
    var users = List.of(User.builder().build());
    var dtos = List.of(UserDto.builder().build());

    when(repository.findAll()).thenReturn(users);
    when(mapper.toDtos(users)).thenReturn(dtos);

    var result = userService.getAll();
    assertEquals(dtos.size(), result.size());
  }

  @Test
  void shouldGetUserById() {
    var userId = UUID.randomUUID().toString();
    var user = User.builder().build();
    var dto = UserDto.builder().build();

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(mapper.toDto(user)).thenReturn(dto);

    var result = userService.get(userId);
    assertEquals(dto, result);
  }

  @Test
  void shouldThrowNotFoundErrorIfUserNotExist() {
    var userId = UUID.randomUUID().toString();
    when(repository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.get(userId));
  }

  @Test
  void shouldUpdateUserIfExists() {
    var userId = UUID.randomUUID().toString();
    var user = User.builder().build();
    var dto = UserDto.builder().login("updatedUser").build();

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(repository.existsByLogin(dto.getLogin())).thenReturn(false);
    when(mapper.toDto(user)).thenReturn(dto);
    when(repository.save(user)).thenReturn(user);

    var result = userService.update(userId, dto);
    assertEquals(dto, result);
  }

  @Test
  void shouldUpdateUserIfLoginEmpty() {
    var userId = UUID.randomUUID().toString();
    var user = User.builder().build();
    var dto = UserDto.builder().login("").build();

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(mapper.toDto(user)).thenReturn(dto);
    when(repository.save(user)).thenReturn(user);

    var result = userService.update(userId, dto);
    assertEquals(dto, result);
  }

  @Test
  void shouldThrowErrorIfUserExists() {
    var userId = UUID.randomUUID().toString();
    var user = User.builder().build();
    var dto = UserDto.builder().login("updatedUser").build();

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(repository.existsByLogin(dto.getLogin())).thenReturn(true);

    assertThrows(UserExistsException.class, () -> userService.update(userId, dto));
  }

  @Test
  void shouldThrowErrorForUpdateIfUserNotFound() {
    var userId = UUID.randomUUID().toString();
    var dto = UserDto.builder().build();
    when(repository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.update(userId, dto));
  }

  @Test
  void shouldThrowErrorForUpdateIfUserExists() {
    var userId = UUID.randomUUID().toString();
    var user = User.builder().build();
    var dto = UserDto.builder().login("existingLogin").build();

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(repository.existsByLogin(dto.getLogin())).thenReturn(true);

    assertThrows(UserExistsException.class, () -> userService.update(userId, dto));
  }

  @Test
  void shouldThrowErrorForCreateIfUserExists() {
    UserDto dto = UserDto.builder().login("existingLogin").password("password").build();

    when(repository.existsByLogin(dto.getLogin())).thenReturn(true);

    assertThrows(UserExistsException.class, () -> userService.create(dto));
  }

  @Test
  void shouldCreateUser() {
    var dto = UserDto.builder().login("newUser").password("password").build();
    var user = User.builder().build();
    var savedUser = User.builder().build();

    when(context.getBean(PasswordEncoder.class)).thenReturn(passwordEncoder);
    when(repository.existsByLogin(dto.getLogin())).thenReturn(false);
    when(mapper.toEntity(dto)).thenReturn(user);
    when(mapper.toDto(savedUser)).thenReturn(dto);
    when(repository.save(user)).thenReturn(savedUser);
    when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

    var result = userService.create(dto);
    assertEquals(dto, result);
  }

  @Test
  void shouldDeleteUser() {
    var userId = UUID.randomUUID().toString();

    assertDoesNotThrow(() -> userService.delete(userId));
    verify(repository).deleteById(userId);
  }

  @Test
  void shouldInitDbIfUserNotExists() {
    when(repository.existsByLogin("admin")).thenReturn(false);
    when(passwordEncoder.encode("adminPassword")).thenReturn("encodedPassword");
    when(context.getBean(PasswordEncoder.class)).thenReturn(passwordEncoder);

    ReflectionTestUtils.setField(userService, "login", "admin");
    ReflectionTestUtils.setField(userService, "password", "adminPassword");

    assertDoesNotThrow(() -> userService.initDB());
    verify(repository).save(any(User.class));
  }

  @ParameterizedTest
  @MethodSource("loginParameters")
  void shouldSkipInitDbIfLoginOrPasswordEmpty(String login, String password) {
    ReflectionTestUtils.setField(userService, "login", login);
    ReflectionTestUtils.setField(userService, "password", password);

    assertDoesNotThrow(() -> userService.initDB());
    verify(repository, never()).save(any(User.class));
  }

  @Test
  void shouldSkipInitDbIfUserExists() {
    when(repository.existsByLogin("admin")).thenReturn(true);

    ReflectionTestUtils.setField(userService, "login", "admin");
    ReflectionTestUtils.setField(userService, "password", "password");

    assertDoesNotThrow(() -> userService.initDB());
    verify(repository, never()).save(any(User.class));
  }

  static Stream<Arguments> loginParameters() {
    return Stream.of(
        Arguments.of("", "adminPassword"),
        Arguments.of("admin", "")
    );
  }
}
