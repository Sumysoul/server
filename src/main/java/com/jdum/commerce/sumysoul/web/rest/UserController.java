package com.jdum.commerce.sumysoul.web.rest;

import com.jdum.commerce.sumysoul.dto.UserDto;
import com.jdum.commerce.sumysoul.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class UserController {

  private final UserService userService;

  @GetMapping
  public List<UserDto> getAll() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  public UserDto get(@PathVariable("id") String id) {
    return userService.get(id);
  }

  @PutMapping("/{id}")
  public UserDto updateUser(@PathVariable("id") String id, @RequestBody UserDto user) {
    return userService.update(id, user);
  }

  @PostMapping
  public UserDto createUser(@RequestBody UserDto user) {
    return userService.create(user);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable("id") String id) {
    userService.delete(id);
  }
}
