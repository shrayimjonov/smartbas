package uz.davrbank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.davrbank.dto.request.MessageRequestDto;
import uz.davrbank.dto.response.MessageResponse;
import uz.davrbank.entity.Message;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageRequestDto requestDto);

    @Mapping(target = "photoId", source = "savedMessage.photoUpload.id")
    MessageResponse toDto(Message savedMessage);

    @Mapping(target = "photoId", source = "photoUpload.id")
    List<MessageResponse> toDtoList(List<Message> messages);
}
