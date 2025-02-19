package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link GoodHarvestHive} and its DTO {@link GoodHarvestHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class, HarvestTypeMapper.class})
public interface GoodHarvestHiveMapper extends EntityMapper<GoodHarvestHiveDTO, GoodHarvestHive> {
  @Named("goodHarvestHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  GoodHarvestHiveDTO toDto(GoodHarvestHive s);

  @Named("goodHarvestHiveToDtos")
  default List<GoodHarvestHiveDTO> toDto(List<GoodHarvestHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("goodHarvestHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  GoodHarvestHive toEntity(GoodHarvestHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget GoodHarvestHive entity, GoodHarvestHiveDTO dto);
}
