package uz.davrbank.educontrolservice.service.cases.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.security.role.RoleRequestDTO;
import uz.davrbank.educontrolservice.feign.security.RoleInterfaceFeign;
import uz.davrbank.educontrolservice.service.dao.security.RoleInterface;

@Slf4j
@Service
public class RoleService implements RoleInterface {
    private final RoleInterfaceFeign roleInterfaceFeign;

    public RoleService(RoleInterfaceFeign roleInterfaceFeign) {
        this.roleInterfaceFeign = roleInterfaceFeign;
    }

    @Override
    public Object getAlLRoles() {
        log.info("REQUEST (getAlLRoles) FROM [security-service]");
        return roleInterfaceFeign.getAllRoles();
    }

    @Override
    public Object createRole(RoleRequestDTO requestDTO) {
        log.info("REQUEST (createRole) FROM [security-service]");
        return roleInterfaceFeign.createRole(requestDTO);
    }

    @Override
    public Object updateRole(String roleName, RoleRequestDTO requestDTO) {
        log.info("REQUEST (updateRole) FROM [security-service]");
        return roleInterfaceFeign.updateRole(roleName, requestDTO);
    }

    @Override
    public Object deleteRole(String roleName) {
        log.info("REQUEST (deleteRole) FROM [security-service]");
        return roleInterfaceFeign.deleteRole(roleName);
    }
}
