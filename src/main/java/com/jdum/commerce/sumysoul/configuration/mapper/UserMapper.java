package com.jdum.commerce.sumysoul.configuration.mapper;

import com.jdum.commerce.sumysoul.domain.User;
import com.jdum.commerce.sumysoul.dto.UserDto;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "password", ignore = true)
  UserDto toDto(User domain);

  User toEntity(UserDto dto);

  List<UserDto> toDtos(List<User> users);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(@MappingTarget User entity, UserDto dto);
}
