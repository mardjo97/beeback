package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.ReproductionHiveRepository;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;
import rs.hexatech.beeback.service.mapper.ReproductionHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.ReproductionHive}.
 */
@Service
@Transactional
public class ReproductionHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(ReproductionHiveService.class);

  private final ReproductionHiveRepository repository;

  private final ReproductionHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public ReproductionHiveService(ReproductionHiveRepository reproductionHiveRepository, ReproductionHiveMapper reproductionHiveMapper) {
    this.repository = reproductionHiveRepository;
    this.mapper = reproductionHiveMapper;
  }

  public List<ReproductionHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's ReproductionHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<ReproductionHiveDTO> sync(List<ReproductionHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private ReproductionHive syncEntity(final ReproductionHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      ReproductionHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        ReproductionHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    ReproductionHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    ReproductionHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final ReproductionHive entity, final ReproductionHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final ReproductionHive entity, final ReproductionHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final ReproductionHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(ReproductionHive reproductionHive, ReproductionHiveDTO reproductionHiveDto, User user) {
    try {
      String hiveUUID = reproductionHiveDto.getHive() != null ? reproductionHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = reproductionHiveDto.getHive() != null ? reproductionHiveDto.getHive().getId() : null;
      reproductionHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the reproductionHive: {}", reproductionHiveDto);
      return false;
    }
    return true;
  }

  /**
   * Save a reproductionHive.
   *
   * @param reproductionHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public ReproductionHiveDTO save(ReproductionHiveDTO reproductionHiveDTO) {
    LOG.debug("Request to save ReproductionHive : {}", reproductionHiveDTO);
    ReproductionHive reproductionHive = mapper.toEntity(reproductionHiveDTO);
    reproductionHive = repository.save(reproductionHive);
    return mapper.toDto(reproductionHive);
  }

  /**
   * Update a reproductionHive.
   *
   * @param reproductionHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public ReproductionHiveDTO update(ReproductionHiveDTO reproductionHiveDTO) {
    LOG.debug("Request to update ReproductionHive : {}", reproductionHiveDTO);
    ReproductionHive reproductionHive = mapper.toEntity(reproductionHiveDTO);
    reproductionHive = repository.save(reproductionHive);
    return mapper.toDto(reproductionHive);
  }

  /**
   * Partially update a reproductionHive.
   *
   * @param reproductionHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<ReproductionHiveDTO> partialUpdate(ReproductionHiveDTO reproductionHiveDTO) {
    LOG.debug("Request to partially update ReproductionHive : {}", reproductionHiveDTO);

    return repository
        .findById(reproductionHiveDTO.getId())
        .map(existingReproductionHive -> {
          mapper.partialUpdate(existingReproductionHive, reproductionHiveDTO);

          return existingReproductionHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the reproductionHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<ReproductionHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all ReproductionHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one reproductionHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ReproductionHiveDTO> findOne(Long id) {
    LOG.debug("Request to get ReproductionHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the reproductionHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete ReproductionHive : {}", id);
    repository.deleteById(id);
  }
}
