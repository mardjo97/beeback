package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Queen;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.QueenRepository;
import rs.hexatech.beeback.service.dto.QueenDTO;
import rs.hexatech.beeback.service.mapper.QueenMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Queen}.
 */
@Service
@Transactional
public class QueenService {

  private static final Logger LOG = LoggerFactory.getLogger(QueenService.class);

  private final QueenRepository repository;

  private final QueenMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public QueenService(QueenRepository queenRepository, QueenMapper queenMapper) {
    this.repository = queenRepository;
    this.mapper = queenMapper;
  }

  public List<QueenDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Queens! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<QueenDTO> sync(List<QueenDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Queen syncEntity(final QueenDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      Queen existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        Queen mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    Queen existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    Queen toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final Queen entity, final QueenDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final Queen entity, final QueenDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final Queen entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(Queen entity, QueenDTO entityDto, User user) {
    try {
      String hiveUUID = entityDto.getHive() != null ? entityDto.getHive().getUuid() : null;
      Long hiveExternalId = entityDto.getHive() != null ? entityDto.getHive().getId() : null;
      entity.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map Hive for the Queen: {}", entityDto);
      return false;
    }
    return true;
  }

  /**
   * Save a queen.
   *
   * @param queenDTO the entity to save.
   * @return the persisted entity.
   */
  public QueenDTO save(QueenDTO queenDTO) {
    LOG.debug("Request to save Queen : {}", queenDTO);
    Queen queen = mapper.toEntity(queenDTO);
    queen = repository.save(queen);
    return mapper.toDto(queen);
  }

  /**
   * Update a queen.
   *
   * @param queenDTO the entity to save.
   * @return the persisted entity.
   */
  public QueenDTO update(QueenDTO queenDTO) {
    LOG.debug("Request to update Queen : {}", queenDTO);
    Queen queen = mapper.toEntity(queenDTO);
    queen = repository.save(queen);
    return mapper.toDto(queen);
  }

  /**
   * Partially update a queen.
   *
   * @param queenDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<QueenDTO> partialUpdate(QueenDTO queenDTO) {
    LOG.debug("Request to partially update Queen : {}", queenDTO);

    return repository
        .findById(queenDTO.getId())
        .map(existingQueen -> {
          mapper.partialUpdate(existingQueen, queenDTO);

          return existingQueen;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the queens.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<QueenDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all Queens");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one queen by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<QueenDTO> findOne(Long id) {
    LOG.debug("Request to get Queen : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the queen by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Queen : {}", id);
    repository.deleteById(id);
  }
}
