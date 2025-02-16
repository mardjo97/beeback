package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;

import java.util.List;

/**
 * Mapper for the entity {@link HiveLocation} and its DTO {@link HiveLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface HiveLocationMapper extends EntityMapper<HiveLocationDTO, HiveLocation> {
  @Named("hiveLocationToDto")
  @Mapping(target = "id", source = "externalId")
  HiveLocationDTO toDto(HiveLocation s);

  @Named("hiveLocationToDtos")
  default List<HiveLocationDTO> toDto(List<HiveLocation> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("hiveLocationToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  HiveLocation toEntity(HiveLocationDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget HiveLocation entity, HiveLocationDTO dto);
}
