package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link HarvestType} and its DTO {@link HarvestTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface HarvestTypeMapper extends EntityMapper<HarvestTypeDTO, HarvestType> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    HarvestTypeDTO toDto(HarvestType s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
