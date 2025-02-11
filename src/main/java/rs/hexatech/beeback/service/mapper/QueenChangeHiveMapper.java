package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link QueenChangeHive} and its DTO {@link QueenChangeHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface QueenChangeHiveMapper extends EntityMapper<QueenChangeHiveDTO, QueenChangeHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    QueenChangeHiveDTO toDto(QueenChangeHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
