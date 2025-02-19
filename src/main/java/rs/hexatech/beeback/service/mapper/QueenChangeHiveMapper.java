package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link QueenChangeHive} and its DTO {@link QueenChangeHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface QueenChangeHiveMapper extends EntityMapper<QueenChangeHiveDTO, QueenChangeHive> {
  @Named("queenChangeHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  QueenChangeHiveDTO toDto(QueenChangeHive s);

  @Named("queenChangeHiveToDtos")
  default List<QueenChangeHiveDTO> toDto(List<QueenChangeHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("queenChangeHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  QueenChangeHive toEntity(QueenChangeHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget QueenChangeHive entity, QueenChangeHiveDTO dto);
}
