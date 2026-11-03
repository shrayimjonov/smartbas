package uz.davrbank.yeisvoservice.aggregate.mapper.dictionary;

import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.IndustrialZoneResponse;
import uz.davrbank.yeisvoservice.entity.spravochniki.IndustrialZoneEntity;
import org.springframework.stereotype.Component;

@Component
public class IndustrialZoneMapper {
    public  IndustrialZoneEntity toEntity(IndustrialZoneResponse.IndustrialZone industrialZone) {
        IndustrialZoneEntity entity = new IndustrialZoneEntity();

        entity.setIdNumber(industrialZone.getId());
        entity.setName(industrialZone.getName());
        entity.setIsDeleted(industrialZone.getIsDeleted());
        return entity;
    }

    public IndustrialZoneEntity update(IndustrialZoneResponse.IndustrialZone externalResponse, IndustrialZoneEntity existingOpt) {
        existingOpt.setName(externalResponse.getName());
        existingOpt.setIsDeleted(externalResponse.getIsDeleted());
        return existingOpt;
    }
    public  IndustrialZoneResponse.IndustrialZone toDto(IndustrialZoneEntity save) {
        IndustrialZoneResponse.IndustrialZone res = new IndustrialZoneResponse.IndustrialZone();
        res.setId(save.getIdNumber());
        res.setIdNumber(save.getId());
        res.setName(save.getName());
        res.setIsDeleted(save.getIsDeleted());
        return res;
    }
}
