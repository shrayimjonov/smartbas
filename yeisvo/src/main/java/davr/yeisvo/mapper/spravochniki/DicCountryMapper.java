package davr.yeisvo.mapper.spravochniki;

import davr.yeisvo.dto.response.spravochniki.DicCountryResponse;
import davr.yeisvo.entity.postgres.spravochniki.DicCountryEntity;
import org.springframework.stereotype.Component;

@Component
public class DicCountryMapper {
    public DicCountryEntity toEntity(DicCountryResponse.Records record) {
        DicCountryEntity entity = new DicCountryEntity();

        entity.setIdNumber(record.getId());
        entity.setName(record.getName());
        entity.setIsDeleted(record.getIsDeleted());
        return entity;
    }

    public DicCountryResponse.Records toDto(DicCountryEntity save) {
        DicCountryResponse.Records dicCountryRes = new DicCountryResponse.Records();
        dicCountryRes.setIdNumber(save.getId());
        dicCountryRes.setId(save.getIdNumber());
        dicCountryRes.setName(save.getName());
        dicCountryRes.setIsDeleted(save.getIsDeleted());
        return dicCountryRes;
    }
}
