package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.ApiaryDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Apiary} and its DTO {@link ApiaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiaryMapper extends EntityMapper<ApiaryDTO, Apiary> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ApiaryDTO toDto(Apiary s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
