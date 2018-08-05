package com.adammendak.authentication.model.mapper;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser (UserDto userDto);

    UserDto userToUserDto (User user);

}
