package com.jdum.commerce.sumysoul.service;

import com.jdum.commerce.sumysoul.domain.Authority;
import com.jdum.commerce.sumysoul.configuration.mapper.UserMapper;
import com.jdum.commerce.sumysoul.domain.User;
import com.jdum.commerce.sumysoul.dto.UserDto;
import com.jdum.commerce.sumysoul.repository.UserRepository;
import com.jdum.commerce.sumysoul.web.error.NotFoundException;
import com.jdum.commerce.sumysoul.web.error.UnauthorizedException;
import com.jdum.commerce.sumysoul.web.error.UserExistsException;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

  @Value("${APP_INIT_USER_LOGIN:#{null}}")
  private String login;

  @Value("${APP_INIT_USER_PASSWORD:#{null}}")
  private String password;

  private final UserRepository repository;
  private final UserMapper mapper;
  private final ApplicationContext context;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findOneByLogin(username)
        .orElseThrow(
            () -> new UnauthorizedException(String.format("User %s not found", username)));

    List<SimpleGrantedAuthority> authorities = user.getAuthorities()
        .stream()
        .map(Enum::name)
        .map(SimpleGrantedAuthority::new)
        .toList();

    return new org.springframework.security.core.userdetails.User(user.getLogin(),
        user.getPassword(), authorities);
  }

  public List<UserDto> getAll() {
    return mapper.toDtos(repository.findAll());
  }

  public UserDto get(String id) {
    return repository.findById(id)
        .map(mapper::toDto)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public UserDto update(String id, UserDto dto) {
    var entity =
        repository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
    if (StringUtils.hasText(dto.getLogin()) && repository.existsByLogin(dto.getLogin())) {
      throw new UserExistsException("User already exists");
    }
    mapper.updateEntity(entity, dto);
    return mapper.toDto(repository.save(entity));
  }

  public UserDto create(UserDto dto) {
    if (repository.existsByLogin(dto.getLogin())) {
      throw new UserExistsException("User already exists");
    }
    var passwordEncoder = context.getBean(PasswordEncoder.class);
    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    User entity = mapper.toEntity(dto);
    User saved = repository.save(entity);
    return mapper.toDto(saved);
  }

  public void delete(String id) {
    repository.deleteById(id);
  }

  public void initDB() {
    if (StringUtils.hasText(login) &&
        StringUtils.hasLength(password) &&
        !repository.existsByLogin(login)) {
      log.info("Initializing user DB starts");
      repository.save(
          User.builder()
              .login(login)
              .password(context.getBean(PasswordEncoder.class).encode(password))
              .authorities(EnumSet.of(Authority.SUPER_ADMIN))
              .build()
      );
      log.info("Initializing user DB completed");
      return;
    }
    log.info("Skip initializing user DB");
  }
}
