package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Note;
import rs.hexatech.beeback.service.dto.NoteDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
  @Named("noteToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  NoteDTO toDto(Note s);

  @Named("noteToDtos")
  default List<NoteDTO> toDto(List<Note> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("noteToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Note toEntity(NoteDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Note entity, NoteDTO dto);
}
