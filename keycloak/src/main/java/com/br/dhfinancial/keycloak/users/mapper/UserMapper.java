package com.br.dhfinancial.keycloak.users.mapper;

import com.br.dhfinancial.keycloak.users.dto.request.UserRequest;
import com.br.dhfinancial.keycloak.users.dto.response.UserResponse;
import com.br.dhfinancial.keycloak.users.model.User;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRepresentation fromUserToUserRepresentation(User user);
    User fromUserRepresentationToUser(UserRepresentation userRepresentation);
    User fromUserResourceToUser(UserResource userResource);

    UserResponse fromUserToUserResponse(User user);
    User fromUserRequestToUser(UserRequest userRequest);
}
