package davr.yeisvo.mapper;

import davr.yeisvo.dto.request.BankBranchRequest;
import davr.yeisvo.dto.response.BankBranchResponse;
import davr.yeisvo.entity.postgres.BankBranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BankBranchMapper {
    public static BankBranchEntity toEntity(BankBranchRequest request) {
        return BankBranchEntity.builder()
                .inn(request.getInn())
                .mfo(request.getMfo())
                .name(request.getName())
                .build();
    }

    public static BankBranchResponse toResponse(BankBranchEntity entity) {
        return new BankBranchResponse(
                entity.getId(),
                entity.getInn(),
                entity.getMfo(),
                entity.getName(),
                entity.getIsActive()
        );
    }
}
