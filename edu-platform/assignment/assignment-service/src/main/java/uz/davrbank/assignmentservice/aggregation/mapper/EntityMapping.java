package uz.davrbank.assignmentservice.aggregation.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface EntityMapping<E, Q, P> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toEntityFromReq(Q request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toEntityFromRes(P response);

    P toDto(E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromReqDto(Q request, @MappingTarget E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromResDto(P res, @MappingTarget E entity);

    List<E> toEntityList(List<Q> requestList);

    List<P> toDtoList(List<E> entityList);

    List<E> toEntityFromResponseDTOList(List<P> responseDTOList);
}
