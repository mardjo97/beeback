package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.FeedingHiveRepository;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;
import rs.hexatech.beeback.service.mapper.FeedingHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.FeedingHive}.
 */
@Service
@Transactional
public class FeedingHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(FeedingHiveService.class);

  private final FeedingHiveRepository repository;

  private final FeedingHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public FeedingHiveService(FeedingHiveRepository feedingHiveRepository, FeedingHiveMapper feedingHiveMapper) {
    this.repository = feedingHiveRepository;
    this.mapper = feedingHiveMapper;
  }

  public List<FeedingHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's FeedingHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<FeedingHiveDTO> sync(List<FeedingHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private FeedingHive syncEntity(final FeedingHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      FeedingHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        FeedingHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    FeedingHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    FeedingHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final FeedingHive entity, final FeedingHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final FeedingHive entity, final FeedingHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final FeedingHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(FeedingHive feedingHive, FeedingHiveDTO feedingHiveDto, User user) {
    try {
      String hiveUUID = feedingHiveDto.getHive() != null ? feedingHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = feedingHiveDto.getHive() != null ? feedingHiveDto.getHive().getId() : null;
      feedingHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the feedingHive: {}", feedingHiveDto);
      return false;
    }
    return true;
  }

  /**
   * Save a feedingHive.
   *
   * @param feedingHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public FeedingHiveDTO save(FeedingHiveDTO feedingHiveDTO) {
    LOG.debug("Request to save FeedingHive : {}", feedingHiveDTO);
    FeedingHive feedingHive = mapper.toEntity(feedingHiveDTO);
    feedingHive = repository.save(feedingHive);
    return mapper.toDto(feedingHive);
  }

  /**
   * Update a feedingHive.
   *
   * @param feedingHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public FeedingHiveDTO update(FeedingHiveDTO feedingHiveDTO) {
    LOG.debug("Request to update FeedingHive : {}", feedingHiveDTO);
    FeedingHive feedingHive = mapper.toEntity(feedingHiveDTO);
    feedingHive = repository.save(feedingHive);
    return mapper.toDto(feedingHive);
  }

  /**
   * Partially update a feedingHive.
   *
   * @param feedingHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<FeedingHiveDTO> partialUpdate(FeedingHiveDTO feedingHiveDTO) {
    LOG.debug("Request to partially update FeedingHive : {}", feedingHiveDTO);

    return repository
        .findById(feedingHiveDTO.getId())
        .map(existingFeedingHive -> {
          mapper.partialUpdate(existingFeedingHive, feedingHiveDTO);

          return existingFeedingHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the feedingHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<FeedingHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all FeedingHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one feedingHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<FeedingHiveDTO> findOne(Long id) {
    LOG.debug("Request to get FeedingHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the feedingHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete FeedingHive : {}", id);
    repository.deleteById(id);
  }
}
