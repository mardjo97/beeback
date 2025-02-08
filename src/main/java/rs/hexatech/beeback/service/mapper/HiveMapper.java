package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.service.dto.HiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Hive} and its DTO {@link HiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveTypeMapper.class, ApiaryMapper.class})
public interface HiveMapper extends EntityMapper<HiveDTO, Hive> {

  @Named("hiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hiveType", source = "hiveType", qualifiedByName = "hiveTypeToDto")
  @Mapping(target = "beeyard", source = "apiary", qualifiedByName = "apiaryToDto")
  HiveDTO toDto(Hive s);

  @Named("hiveToDtos")
  default List<HiveDTO> toDto(List<Hive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("hiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hiveType", source = "hiveType", qualifiedByName = "hiveTypeToEntity")
  @Mapping(target = "apiary", source = "beeyard", qualifiedByName = "apiaryToEntity")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Hive toEntity(HiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "apiary", ignore = true)
  @Mapping(target = "hiveType", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Hive entity, HiveDTO dto);
}
