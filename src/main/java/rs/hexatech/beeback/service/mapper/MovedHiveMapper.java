package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.dto.HiveDTO;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link MovedHive} and its DTO {@link MovedHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovedHiveMapper extends EntityMapper<MovedHiveDTO, MovedHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveId")
    @Mapping(target = "harvestType", source = "harvestType", qualifiedByName = "harvestTypeId")
    MovedHiveDTO toDto(MovedHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("hiveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HiveDTO toDtoHiveId(Hive hive);

    @Named("harvestTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HarvestTypeDTO toDtoHarvestTypeId(HarvestType harvestType);
}
