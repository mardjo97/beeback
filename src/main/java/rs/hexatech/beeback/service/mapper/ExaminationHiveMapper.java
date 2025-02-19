package rs.hexatech.beeback.service.mapper;

import org.mapstruct.*;
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;
import rs.hexatech.beeback.service.dto.HiveDTO;
import rs.hexatech.beeback.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ExaminationHive} and its DTO {@link ExaminationHiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExaminationHiveMapper extends EntityMapper<ExaminationHiveDTO, ExaminationHive> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveId")
    ExaminationHiveDTO toDto(ExaminationHive s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("hiveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HiveDTO toDtoHiveId(Hive hive);
}
