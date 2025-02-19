package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link MovedHive} and its DTO {@link MovedHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class, HarvestTypeMapper.class})
public interface MovedHiveMapper extends EntityMapper<MovedHiveDTO, MovedHive> {
  @Named("movedHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  @Mapping(target = "harvestType", source = "harvestType", qualifiedByName = "harvestTypeToDto")
  MovedHiveDTO toDto(MovedHive s);

  @Named("movedHiveToDtos")
  default List<MovedHiveDTO> toDto(List<MovedHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("movedHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  MovedHive toEntity(MovedHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget MovedHive entity, MovedHiveDTO dto);
}
