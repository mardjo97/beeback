package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.GroupDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Group} and its DTO {@link GroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    GroupDTO toDto(Group s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
