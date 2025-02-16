package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.AppConfig;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.AppConfigRepository;
import rs.hexatech.beeback.service.dto.AppConfigDTO;
import rs.hexatech.beeback.service.mapper.AppConfigMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.AppConfig}.
 */
@Service
@Transactional
public class AppConfigService {

  private static final Logger LOG = LoggerFactory.getLogger(AppConfigService.class);

  private final AppConfigRepository repository;

  private final AppConfigMapper mapper;

  @Autowired
  SecurityService securityService;

  public AppConfigService(AppConfigRepository appConfigRepository, AppConfigMapper appConfigMapper) {
    this.repository = appConfigRepository;
    this.mapper = appConfigMapper;
  }

  public List<AppConfigDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's AppConfigs! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(getUserAppConfigsOrDefault(user));
  }

  public List<AppConfigDTO> sync(List<AppConfigDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private AppConfig syncEntity(final AppConfigDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      AppConfig existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        AppConfig mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    AppConfig existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    AppConfig toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final AppConfig entity, final AppConfigDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private boolean toCreate(final AppConfig entity, final AppConfigDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private void toReset(final AppConfig entity) {
    entity.uuid(null).dateSynched(null);
  }

  private List<AppConfig> getUserAppConfigsOrDefault(User user) {
    List<AppConfig> appConfigs = repository.findByUserIsCurrentUser();
    if (appConfigs.isEmpty()) {
      List<AppConfig> hiveTypesToCreate = repository.findByUserIsNull().stream()
          .filter(Objects::nonNull)
          .map(el -> mapAppConfigToCreate(el, user))
          .toList();
      appConfigs = repository.saveAll(hiveTypesToCreate);
    }
    return appConfigs;
  }

  private AppConfig mapAppConfigToCreate(AppConfig appConfig, User user) {
    return new AppConfig()
        .user(user)
        .uuid(UUID.randomUUID().toString())
        .dateCreated(DateTimeUtil.now())
        .dateModified(DateTimeUtil.now())
        .dateSynched(DateTimeUtil.now())
        .externalId(appConfig.getExternalId())
        .key(appConfig.getKey())
        .type(appConfig.getType())
        .value(appConfig.getValue());
  }

  /**
   * Save a appConfig.
   *
   * @param appConfigDTO the entity to save.
   * @return the persisted entity.
   */
  public AppConfigDTO save(AppConfigDTO appConfigDTO) {
    LOG.debug("Request to save AppConfig : {}", appConfigDTO);
    AppConfig appConfig = mapper.toEntity(appConfigDTO);
    appConfig = repository.save(appConfig);
    return mapper.toDto(appConfig);
  }

  /**
   * Update a appConfig.
   *
   * @param appConfigDTO the entity to save.
   * @return the persisted entity.
   */
  public AppConfigDTO update(AppConfigDTO appConfigDTO) {
    LOG.debug("Request to update AppConfig : {}", appConfigDTO);
    AppConfig appConfig = mapper.toEntity(appConfigDTO);
    appConfig = repository.save(appConfig);
    return mapper.toDto(appConfig);
  }

  /**
   * Partially update a appConfig.
   *
   * @param appConfigDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<AppConfigDTO> partialUpdate(AppConfigDTO appConfigDTO) {
    LOG.debug("Request to partially update AppConfig : {}", appConfigDTO);

    return repository
        .findById(appConfigDTO.getId())
        .map(existingAppConfig -> {
          mapper.partialUpdate(existingAppConfig, appConfigDTO);

          return existingAppConfig;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the appConfigs.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<AppConfigDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all AppConfigs");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one appConfig by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AppConfigDTO> findOne(Long id) {
    LOG.debug("Request to get AppConfig : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the appConfig by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete AppConfig : {}", id);
    repository.deleteById(id);
  }
}
