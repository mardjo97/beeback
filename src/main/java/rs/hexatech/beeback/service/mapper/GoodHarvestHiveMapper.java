package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link GoodHarvestHive} and its DTO {@link GoodHarvestHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface GoodHarvestHiveMapper extends EntityMapper<GoodHarvestHiveDTO, GoodHarvestHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "harvestType", source = "harvestType", qualifiedByName = "harvestTypeId")
    GoodHarvestHiveDTO toDto(GoodHarvestHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("harvestTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HarvestTypeDTO toDtoHarvestTypeId(HarvestType harvestType);
}
