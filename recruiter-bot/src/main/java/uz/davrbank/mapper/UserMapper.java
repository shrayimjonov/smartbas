package uz.davrbank.mapper;

import org.mapstruct.Mapper;
import uz.davrbank.dto.request.UserRequestDto;
import uz.davrbank.dto.response.UserResponse;
import uz.davrbank.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User entity);
    User toEntity(UserRequestDto dto);
}
