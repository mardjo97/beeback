package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link ExaminationHive} and its DTO {@link ExaminationHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface ExaminationHiveMapper extends EntityMapper<ExaminationHiveDTO, ExaminationHive> {
  @Named("examinationHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  ExaminationHiveDTO toDto(ExaminationHive s);

  @Named("examinationHiveToDtos")
  default List<ExaminationHiveDTO> toDto(List<ExaminationHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("examinationHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  ExaminationHive toEntity(ExaminationHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget ExaminationHive entity, ExaminationHiveDTO dto);
}
