package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Queen;
import rs.hexatech.beeback.service.dto.QueenDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Queen} and its DTO {@link QueenDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface QueenMapper extends EntityMapper<QueenDTO, Queen> {

  @Named("queenToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  QueenDTO toDto(Queen s);

  @Named("queenToDtos")
  default List<QueenDTO> toDto(List<Queen> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("queenToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Queen toEntity(QueenDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Queen entity, QueenDTO dto);
}
