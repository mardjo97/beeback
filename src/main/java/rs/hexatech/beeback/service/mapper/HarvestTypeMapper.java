package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;

import java.util.List;

/**
 * Mapper for the entity {@link HarvestType} and its DTO {@link HarvestTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface HarvestTypeMapper extends EntityMapper<HarvestTypeDTO, HarvestType> {
  @Named("harvestTypeToDto")
  @Mapping(target = "id", source = "externalId")
  HarvestTypeDTO toDto(HarvestType s);

  @Named("harvestTypeToDtos")
  default List<HarvestTypeDTO> toDto(List<HarvestType> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("harvestTypeToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  HarvestType toEntity(HarvestTypeDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget HarvestType entity, HarvestTypeDTO dto);
}
