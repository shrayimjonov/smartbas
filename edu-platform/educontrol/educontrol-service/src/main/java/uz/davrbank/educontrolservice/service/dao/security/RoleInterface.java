package uz.davrbank.educontrolservice.service.dao.security;

import uz.davrbank.educontrolservice.aggregate.dto.security.role.RoleRequestDTO;

public interface RoleInterface {

    Object getAlLRoles();

    Object createRole(RoleRequestDTO requestDTO);

    Object updateRole(String roleName, RoleRequestDTO requestDTO);

    Object deleteRole(String roleName);
}
