package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link FeedingHive} and its DTO {@link FeedingHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedingHiveMapper extends EntityMapper<FeedingHiveDTO, FeedingHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FeedingHiveDTO toDto(FeedingHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
