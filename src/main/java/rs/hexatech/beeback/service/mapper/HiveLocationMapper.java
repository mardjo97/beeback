package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link HiveLocation} and its DTO {@link HiveLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface HiveLocationMapper extends EntityMapper<HiveLocationDTO, HiveLocation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    HiveLocationDTO toDto(HiveLocation s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
