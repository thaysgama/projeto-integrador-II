package com.br.digitalmoneyhouse.user.service;

import com.br.digitalmoneyhouse.user.dto.*;
import com.br.digitalmoneyhouse.user.model.UserEntity;

public interface UserService {
    Boolean existsByUsername(String username);
    UserResponse save(UserDTO userDTO, String roleName);
    void recoverUser(RecoverDTO username);
    void codeRecoverUser(CodeRecoverDTO code, Long id);
    Boolean confirmEmail(String code);
    UserDetailResponse describeUser(Long id);
    UserDetailResponse updateUser(Long id, UserDTOUpdate userDTO);
    UserEntity findById(Long id);
}
