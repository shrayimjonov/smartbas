package uz.davrbank.educontrolservice.service.dao.security;

import uz.davrbank.educontrolservice.aggregate.dto.security.role.RolesRequestDTO;

import java.util.List;

public interface MergeInterface {

    Object mergeRoleToGroup(String groupId, List<RolesRequestDTO> rolesRequestDTOS);

    Object mergeUserToGroup(String userId, String groupId);

    Object deleteRolesGroupGroup(String groupId, List<RolesRequestDTO> rolesRequestDTOS);

    Object deleteUserFromGroup(String userId, String groupId);
}
