package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.ExaminationHiveRepository;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;
import rs.hexatech.beeback.service.mapper.ExaminationHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.ExaminationHive}.
 */
@Service
@Transactional
public class ExaminationHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(ExaminationHiveService.class);

  private final ExaminationHiveRepository repository;

  private final ExaminationHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  public ExaminationHiveService(ExaminationHiveRepository examinationHiveRepository, ExaminationHiveMapper examinationHiveMapper) {
    this.repository = examinationHiveRepository;
    this.mapper = examinationHiveMapper;
  }

  public List<ExaminationHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's ExaminationHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<ExaminationHiveDTO> sync(List<ExaminationHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private ExaminationHive syncEntity(final ExaminationHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      ExaminationHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        ExaminationHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    ExaminationHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    ExaminationHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final ExaminationHive entity, final ExaminationHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final ExaminationHive entity, final ExaminationHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final ExaminationHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(ExaminationHive examinationHive, ExaminationHiveDTO examinationHiveDto, User user) {
    try {
      String hiveUUID = examinationHiveDto.getHive() != null ? examinationHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = examinationHiveDto.getHive() != null ? examinationHiveDto.getHive().getId() : null;
      examinationHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the examinationHive: {}", examinationHiveDto);
      return false;
    }
    return true;
  }


  /**
   * Save a examinationHive.
   *
   * @param examinationHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public ExaminationHiveDTO save(ExaminationHiveDTO examinationHiveDTO) {
    LOG.debug("Request to save ExaminationHive : {}", examinationHiveDTO);
    ExaminationHive examinationHive = mapper.toEntity(examinationHiveDTO);
    examinationHive = repository.save(examinationHive);
    return mapper.toDto(examinationHive);
  }

  /**
   * Update a examinationHive.
   *
   * @param examinationHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public ExaminationHiveDTO update(ExaminationHiveDTO examinationHiveDTO) {
    LOG.debug("Request to update ExaminationHive : {}", examinationHiveDTO);
    ExaminationHive examinationHive = mapper.toEntity(examinationHiveDTO);
    examinationHive = repository.save(examinationHive);
    return mapper.toDto(examinationHive);
  }

  /**
   * Partially update a examinationHive.
   *
   * @param examinationHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<ExaminationHiveDTO> partialUpdate(ExaminationHiveDTO examinationHiveDTO) {
    LOG.debug("Request to partially update ExaminationHive : {}", examinationHiveDTO);

    return repository
        .findById(examinationHiveDTO.getId())
        .map(existingExaminationHive -> {
          mapper.partialUpdate(existingExaminationHive, examinationHiveDTO);

          return existingExaminationHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the examinationHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<ExaminationHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all ExaminationHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one examinationHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ExaminationHiveDTO> findOne(Long id) {
    LOG.debug("Request to get ExaminationHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the examinationHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete ExaminationHive : {}", id);
    repository.deleteById(id);
  }
}
