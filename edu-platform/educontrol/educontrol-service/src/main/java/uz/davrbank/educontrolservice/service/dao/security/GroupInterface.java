package uz.davrbank.educontrolservice.service.dao.security;

import uz.davrbank.educontrolservice.aggregate.dto.security.group.GroupRequestDTO;

import java.util.List;

public interface GroupInterface {

    List<Object> getAllGroups();

    Object createGroup(GroupRequestDTO groupRequestDTO);

    Object getGroupInfo(String id);

    List<Object> getGroupMembers(String id);

    Object updateGroup(String id, GroupRequestDTO groupRequestDTO);

    Object deleteGroup(String id);


}
