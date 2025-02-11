package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ReproductionHive} and its DTO {@link ReproductionHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReproductionHiveMapper extends EntityMapper<ReproductionHiveDTO, ReproductionHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ReproductionHiveDTO toDto(ReproductionHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
