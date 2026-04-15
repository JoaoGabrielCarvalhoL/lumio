package br.com.joaogabriel.lumio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.UserResponse;
import br.com.joaogabriel.lumio.model.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	
	User toUser(UserCreateRequest userCreateRequest);
	
	UserResponse toUserResponse(User user);

}
