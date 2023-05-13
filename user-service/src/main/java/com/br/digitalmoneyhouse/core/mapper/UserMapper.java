package com.br.digitalmoneyhouse.core.mapper;

import com.br.digitalmoneyhouse.user.dto.UserDTO;
import com.br.digitalmoneyhouse.user.dto.UserDetailResponse;
import com.br.digitalmoneyhouse.user.dto.UserResponse;
import com.br.digitalmoneyhouse.user.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse fromUserEntitytoUserResponse(UserEntity userEntity);
    UserDetailResponse fromUserEntitytoUserDetailResponse(UserEntity userEntity);
    UserEntity fromUserDTOtoUserEntity(UserDTO userDTO);
}
