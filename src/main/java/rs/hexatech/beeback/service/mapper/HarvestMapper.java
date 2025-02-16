package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Harvest;
import rs.hexatech.beeback.service.dto.HarvestDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Harvest} and its DTO {@link HarvestDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface HarvestMapper extends EntityMapper<HarvestDTO, Harvest> {
  @Named("harvestToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  HarvestDTO toDto(Harvest s);

  @Named("harvestToDtos")
  default List<HarvestDTO> toDto(List<Harvest> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("harvestToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Harvest toEntity(HarvestDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "harvestType", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Harvest entity, HarvestDTO dto);
}
