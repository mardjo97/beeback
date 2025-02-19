package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.QueenChangeHiveRepository;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;
import rs.hexatech.beeback.service.mapper.QueenChangeHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.QueenChangeHive}.
 */
@Service
@Transactional
public class QueenChangeHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(QueenChangeHiveService.class);

  private final QueenChangeHiveRepository repository;

  private final QueenChangeHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public QueenChangeHiveService(QueenChangeHiveRepository queenChangeHiveRepository, QueenChangeHiveMapper queenChangeHiveMapper) {
    this.repository = queenChangeHiveRepository;
    this.mapper = queenChangeHiveMapper;
  }

  public List<QueenChangeHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's QueenChangeHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<QueenChangeHiveDTO> sync(List<QueenChangeHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private QueenChangeHive syncEntity(final QueenChangeHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      QueenChangeHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        QueenChangeHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    QueenChangeHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    QueenChangeHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final QueenChangeHive entity, final QueenChangeHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final QueenChangeHive entity, final QueenChangeHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final QueenChangeHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(QueenChangeHive queenChangeHive, QueenChangeHiveDTO queenChangeHiveDto, User user) {
    try {
      String hiveUUID = queenChangeHiveDto.getHive() != null ? queenChangeHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = queenChangeHiveDto.getHive() != null ? queenChangeHiveDto.getHive().getId() : null;
      queenChangeHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the queenChangeHive: {}", queenChangeHiveDto);
      return false;
    }
    return true;
  }

  /**
   * Save a queenChangeHive.
   *
   * @param queenChangeHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public QueenChangeHiveDTO save(QueenChangeHiveDTO queenChangeHiveDTO) {
    LOG.debug("Request to save QueenChangeHive : {}", queenChangeHiveDTO);
    QueenChangeHive queenChangeHive = mapper.toEntity(queenChangeHiveDTO);
    queenChangeHive = repository.save(queenChangeHive);
    return mapper.toDto(queenChangeHive);
  }

  /**
   * Update a queenChangeHive.
   *
   * @param queenChangeHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public QueenChangeHiveDTO update(QueenChangeHiveDTO queenChangeHiveDTO) {
    LOG.debug("Request to update QueenChangeHive : {}", queenChangeHiveDTO);
    QueenChangeHive queenChangeHive = mapper.toEntity(queenChangeHiveDTO);
    queenChangeHive = repository.save(queenChangeHive);
    return mapper.toDto(queenChangeHive);
  }

  /**
   * Partially update a queenChangeHive.
   *
   * @param queenChangeHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<QueenChangeHiveDTO> partialUpdate(QueenChangeHiveDTO queenChangeHiveDTO) {
    LOG.debug("Request to partially update QueenChangeHive : {}", queenChangeHiveDTO);

    return repository
        .findById(queenChangeHiveDTO.getId())
        .map(existingQueenChangeHive -> {
          mapper.partialUpdate(existingQueenChangeHive, queenChangeHiveDTO);

          return existingQueenChangeHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the queenChangeHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<QueenChangeHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all QueenChangeHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one queenChangeHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<QueenChangeHiveDTO> findOne(Long id) {
    LOG.debug("Request to get QueenChangeHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the queenChangeHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete QueenChangeHive : {}", id);
    repository.deleteById(id);
  }
}
