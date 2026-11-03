package uz.davrbank.educontrolservice.service.cases.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.security.group.GroupRequestDTO;
import uz.davrbank.educontrolservice.feign.security.GroupInterfaceFeign;
import uz.davrbank.educontrolservice.service.dao.security.GroupInterface;

import java.util.List;

@Service
@Slf4j
public class GroupService implements GroupInterface {

    private final GroupInterfaceFeign groupInterfaceFeign;

    public GroupService(GroupInterfaceFeign groupInterfaceFeign) {
        this.groupInterfaceFeign = groupInterfaceFeign;
    }

    @Override
    public List<Object> getAllGroups() {
        log.info("REQUEST (getAllGroups) FROM [security-service]");
        return groupInterfaceFeign.getAllGroups();
    }

    @Override
    public Object createGroup(GroupRequestDTO groupRequestDTO) {
        log.info("REQUEST (createGroup) FROM [security-service]");
        return groupInterfaceFeign.createGroup(groupRequestDTO);
    }

    @Override
    public Object getGroupInfo(String id) {
        log.info("REQUEST (getGroupInfo) FROM [security-service]");
        return groupInterfaceFeign.getGroupInfo(id);
    }

    @Override
    public List<Object> getGroupMembers(String id) {
        log.info("REQUEST (getGroupMembers) FROM [security-service]");
        return groupInterfaceFeign.getGroupMembers(id);
    }

    @Override
    public Object updateGroup(String id, GroupRequestDTO groupRequestDTO) {
        log.info("REQUEST (updateGroup) FROM [security-service]");
        return groupInterfaceFeign.updateGroup(id, groupRequestDTO);
    }

    @Override
    public Object deleteGroup(String id) {
        log.info("REQUEST (deleteGroup) FROM [security-service]");
        return groupInterfaceFeign.deleteGroup(id);
    }
}
