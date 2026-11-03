package davr.yeisvo.mapper.spravochniki;

import davr.yeisvo.dto.response.spravochniki.RegionsResponse;
import davr.yeisvo.entity.postgres.spravochniki.RegionsEntity;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {
    public RegionsEntity toEntity(RegionsResponse.Regions regions) {
        RegionsEntity entity = new RegionsEntity();

        entity.setIdNumber(regions.getId());
        entity.setName(regions.getName());
        entity.setIsDeleted(regions.getIsDeleted());
        return entity;
    }

    public RegionsEntity update(RegionsResponse.Regions externalResponse, RegionsEntity existingOpt) {
        existingOpt.setName(externalResponse.getName());
        existingOpt.setIsDeleted(externalResponse.getIsDeleted());
        return existingOpt;
    }

    public static RegionsResponse.Regions toDto(RegionsEntity save) {
        RegionsResponse.Regions res = new RegionsResponse.Regions();
        res.setIdNumber(save.getId());
        res.setId(save.getIdNumber());
        res.setName(save.getName());
        res.setIsDeleted(save.getIsDeleted());
        return res;
    }
}
