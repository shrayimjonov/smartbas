package uz.davrbank.educontrolservice.service.cases.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RolesRequestDTO;
import uz.davrbank.educontrolservice.feign.security.MergeInterfaceFeign;
import uz.davrbank.educontrolservice.service.dao.security.MergeInterface;

import java.util.List;

@Service
@Slf4j
public class MergeService implements MergeInterface {

    private final MergeInterfaceFeign mergeInterfaceFeign;

    public MergeService(MergeInterfaceFeign mergeInterfaceFeign) {
        this.mergeInterfaceFeign = mergeInterfaceFeign;
    }

    @Override
    public Object mergeRoleToGroup(String groupId, List<RolesRequestDTO> rolesRequestDTOS) {
        log.info("REQUEST (mergeRoleToGroup) FROM [security-service]");
        return mergeInterfaceFeign.mergeRolesToGroup(groupId, rolesRequestDTOS);
    }

    @Override
    public Object mergeUserToGroup(String userId, String groupId) {
        log.info("REQUEST (mergeUserToGroup) FROM [security-service]");
        return mergeInterfaceFeign.mergeUserToGroup(userId, groupId);
    }

    @Override
    public Object deleteRolesGroupGroup(String groupId, List<RolesRequestDTO> rolesRequestDTOS) {
        log.info("REQUEST (deleteRolesGroupGroup) FROM [security-service]");
        return mergeInterfaceFeign.deleteRolesFromGroup(groupId, rolesRequestDTOS);
    }

    @Override
    public Object deleteUserFromGroup(String userId, String groupId) {
        log.info("REQUEST (deleteUserFromGroup) FROM [security-service]");
        return mergeInterfaceFeign.deleteUserFromGroup(userId, groupId);
    }
}
