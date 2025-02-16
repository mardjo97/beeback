package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Note;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.NoteRepository;
import rs.hexatech.beeback.service.dto.NoteDTO;
import rs.hexatech.beeback.service.mapper.NoteMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Note}.
 */
@Service
@Transactional
public class NoteService {

  private static final Logger LOG = LoggerFactory.getLogger(NoteService.class);

  private final NoteRepository repository;

  private final NoteMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
    this.repository = noteRepository;
    this.mapper = noteMapper;
  }

  public List<NoteDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Notes! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<NoteDTO> sync(List<NoteDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Note syncEntity(final NoteDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      Note existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        Note mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    Note existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    Note toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final Note entity, final NoteDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final Note entity, final NoteDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final Note entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(Note note, NoteDTO noteDto, User user) {
    try {
      String hiveUUID = noteDto.getHive() != null ? noteDto.getHive().getUuid() : null;
      Long hiveExternalId = noteDto.getHive() != null ? noteDto.getHive().getId() : null;
      note.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the note: {}", noteDto);
      return false;
    }
    return true;
  }

  /**
   * Save a note.
   *
   * @param noteDTO the entity to save.
   * @return the persisted entity.
   */
  public NoteDTO save(NoteDTO noteDTO) {
    LOG.debug("Request to save Note : {}", noteDTO);
    Note note = mapper.toEntity(noteDTO);
    note = repository.save(note);
    return mapper.toDto(note);
  }

  /**
   * Update a note.
   *
   * @param noteDTO the entity to save.
   * @return the persisted entity.
   */
  public NoteDTO update(NoteDTO noteDTO) {
    LOG.debug("Request to update Note : {}", noteDTO);
    Note note = mapper.toEntity(noteDTO);
    note = repository.save(note);
    return mapper.toDto(note);
  }

  /**
   * Partially update a note.
   *
   * @param noteDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<NoteDTO> partialUpdate(NoteDTO noteDTO) {
    LOG.debug("Request to partially update Note : {}", noteDTO);

    return repository
        .findById(noteDTO.getId())
        .map(existingNote -> {
          mapper.partialUpdate(existingNote, noteDTO);

          return existingNote;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the notes.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<NoteDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all Notes");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one note by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<NoteDTO> findOne(Long id) {
    LOG.debug("Request to get Note : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the note by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Note : {}", id);
    repository.deleteById(id);
  }
}
