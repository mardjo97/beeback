package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link ReproductionHive} and its DTO {@link ReproductionHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface ReproductionHiveMapper extends EntityMapper<ReproductionHiveDTO, ReproductionHive> {
  @Named("reproductionHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  ReproductionHiveDTO toDto(ReproductionHive s);

  @Named("reproductionHiveToDtos")
  default List<ReproductionHiveDTO> toDto(List<ReproductionHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("reproductionHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  ReproductionHive toEntity(ReproductionHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget ReproductionHive entity, ReproductionHiveDTO dto);
}
