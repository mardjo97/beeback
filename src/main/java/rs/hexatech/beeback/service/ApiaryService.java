package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.exception.NotFoundException;
import rs.hexatech.beeback.repository.ApiaryRepository;
import rs.hexatech.beeback.service.dto.ApiaryDTO;
import rs.hexatech.beeback.service.mapper.ApiaryMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Apiary}.
 */
@Service
@Transactional
public class ApiaryService {

  private static final Logger LOG = LoggerFactory.getLogger(ApiaryService.class);

  private final ApiaryRepository apiaryRepository;

  private final ApiaryMapper apiaryMapper;

  @Autowired
  private SecurityService securityService;

  public ApiaryService(ApiaryRepository apiaryRepository, ApiaryMapper apiaryMapper) {
    this.apiaryRepository = apiaryRepository;
    this.apiaryMapper = apiaryMapper;
  }

  public Apiary getApiaryByUUIDOrUserAndExternalId(String uuid, User user, Long externalId) {
    if (uuid != null) {
      Apiary e = apiaryRepository.findByUuidIs(uuid);
      if (e != null) {
        return e;
      }
    }
    if (user != null && externalId != null) {
      Apiary e = apiaryRepository.findByUserAndExternalId(user, externalId.intValue());
      if (e != null) {
        return e;
      }
    }
    throw new NotFoundException("Apiary not found for uuid: " + uuid + " or externalId: " + externalId + " and user: " + user);
  }

  /**
   * Retrieve apiaries.
   *
   * @param deviceId to check the device.
   * @return the persisted entity.
   */
  public List<ApiaryDTO> userApiaries(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Apiaries! User {}, DeviceId: {}", user.getLogin(), deviceId);
    //validate deviceId
    return apiaryMapper.toDto(apiaryRepository.findByUserIsCurrentUser());
  }

  /**
   * Sync apiaries.
   *
   * @param apiaryDTOs the entity to save.
   * @return the persisted entity.
   */
  public List<ApiaryDTO> sync(List<ApiaryDTO> apiaryDTOs) {
    LOG.debug("Request to sync Apiaries : {}", apiaryDTOs);

    User user = securityService.getCurrentUser();
    return apiaryDTOs.stream().map(el -> syncApiary(el, user))
        .map(apiaryMapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Apiary syncApiary(final ApiaryDTO apiaryDto, final User user) {
    if (apiaryDto.getUuid() != null) {
      Apiary existingApiary = apiaryRepository.findByUuidIs(apiaryDto.getUuid());
      if (existingApiary == null) {
        LOG.debug("Creating Apiary from client (uuid not on server): {}", apiaryDto.getUuid());
        Apiary mappedApiary = apiaryMapper.toEntity(apiaryDto);
        toCreateFromClient(mappedApiary, user, apiaryDto.getUuid());
        return apiaryRepository.save(mappedApiary);
      }
      apiaryMapper.partialUpdate(existingApiary, apiaryDto);
      toUpdate(existingApiary);
      return apiaryRepository.save(existingApiary);
    }
    Apiary existingApiary = apiaryRepository.findByUserAndExternalId(user, apiaryDto.getId().intValue());
    if (existingApiary != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingApiary, apiaryDto.getUuid());
      LOG.warn("The entity {} is not synched", apiaryDto);
      return null;
    }
    Apiary toCreateApiary = apiaryMapper.toEntity(apiaryDto);
    toCreate(toCreateApiary, user);
    return apiaryRepository.save(toCreateApiary);
  }

  /**
   * Save a apiary.
   *
   * @param apiaryDTO the entity to save.
   * @return the persisted entity.
   */
  public ApiaryDTO save(ApiaryDTO apiaryDTO) {
    LOG.debug("Request to save Apiary : {}", apiaryDTO);
    Apiary apiary = apiaryMapper.toEntity(apiaryDTO);
    apiary = apiaryRepository.save(apiary);
    return apiaryMapper.toDto(apiary);
  }

  /**
   * Update a apiary.
   *
   * @param apiaryDTO the entity to save.
   * @return the persisted entity.
   */
  public ApiaryDTO update(ApiaryDTO apiaryDTO) {
    LOG.debug("Request to update Apiary : {}", apiaryDTO);
    Apiary apiary = apiaryMapper.toEntity(apiaryDTO);
    apiary = apiaryRepository.save(apiary);
    return apiaryMapper.toDto(apiary);
  }

  /**
   * Partially update a apiary.
   *
   * @param apiaryDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<ApiaryDTO> partialUpdate(ApiaryDTO apiaryDTO) {
    LOG.debug("Request to partially update Apiary : {}", apiaryDTO);

    return apiaryRepository
        .findById(apiaryDTO.getId())
        .map(existingApiary -> {
          apiaryMapper.partialUpdate(existingApiary, apiaryDTO);

          return existingApiary;
        })
        .map(apiaryRepository::save)
        .map(apiaryMapper::toDto);
  }

  /**
   * Get all the apiaries.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<ApiaryDTO> findAll(Pageable pageable) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to get all Apiaries for the current user");
    return apiaryRepository.findDistinctByUser_id(pageable, user.getId()).map(apiaryMapper::toDto);
  }

  /**
   * Get one apiary by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ApiaryDTO> findOne(Long id) {
    LOG.debug("Request to get Apiary : {}", id);
    User user = securityService.getCurrentUser();
    LOG.info(user.getLogin());
    return apiaryRepository.findById(id).map(apiaryMapper::toDto);
  }

  /**
   * Delete the apiary by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Apiary : {}", id);
    apiaryRepository.deleteById(id);
  }

  private void toUpdate(final Apiary apiary) {
    apiary.dateSynched(DateTimeUtil.getDateSynced(apiary.getDateModified()));
  }

  private void toCreate(final Apiary apiary, final User user) {
    apiary.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
  }

  private void toCreateFromClient(final Apiary apiary, final User user, final String uuid) {
    apiary.user(user).uuid(uuid).dateSynched(DateTimeUtil.now());
  }

  private void toReset(final Apiary apiary) {
    apiary.uuid(null).dateSynched(null);
  }
}
