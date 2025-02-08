package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.HiveType;
import rs.hexatech.beeback.service.dto.HiveTypeDTO;

import java.util.List;

/**
 * Mapper for the entity {@link HiveType} and its DTO {@link HiveTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface HiveTypeMapper extends EntityMapper<HiveTypeDTO, HiveType> {
  @Named("hiveTypeToDto")
  @Mapping(target = "id", source = "externalId")
  HiveTypeDTO toDto(HiveType s);

  @Named("hiveTypeToDtos")
  default List<HiveTypeDTO> toDto(List<HiveType> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("hiveTypeToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  HiveType toEntity(HiveTypeDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget HiveType entity, HiveTypeDTO dto);
}
