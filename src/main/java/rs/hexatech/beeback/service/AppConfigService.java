package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.AppConfig;
import rs.hexatech.beeback.repository.AppConfigRepository;
import rs.hexatech.beeback.service.dto.AppConfigDTO;
import rs.hexatech.beeback.service.mapper.AppConfigMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.AppConfig}.
 */
@Service
@Transactional
public class AppConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfigService.class);

    private final AppConfigRepository appConfigRepository;

    private final AppConfigMapper appConfigMapper;

    public AppConfigService(AppConfigRepository appConfigRepository, AppConfigMapper appConfigMapper) {
        this.appConfigRepository = appConfigRepository;
        this.appConfigMapper = appConfigMapper;
    }

    /**
     * Save a appConfig.
     *
     * @param appConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public AppConfigDTO save(AppConfigDTO appConfigDTO) {
        LOG.debug("Request to save AppConfig : {}", appConfigDTO);
        AppConfig appConfig = appConfigMapper.toEntity(appConfigDTO);
        appConfig = appConfigRepository.save(appConfig);
        return appConfigMapper.toDto(appConfig);
    }

    /**
     * Update a appConfig.
     *
     * @param appConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public AppConfigDTO update(AppConfigDTO appConfigDTO) {
        LOG.debug("Request to update AppConfig : {}", appConfigDTO);
        AppConfig appConfig = appConfigMapper.toEntity(appConfigDTO);
        appConfig = appConfigRepository.save(appConfig);
        return appConfigMapper.toDto(appConfig);
    }

    /**
     * Partially update a appConfig.
     *
     * @param appConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppConfigDTO> partialUpdate(AppConfigDTO appConfigDTO) {
        LOG.debug("Request to partially update AppConfig : {}", appConfigDTO);

        return appConfigRepository
            .findById(appConfigDTO.getId())
            .map(existingAppConfig -> {
                appConfigMapper.partialUpdate(existingAppConfig, appConfigDTO);

                return existingAppConfig;
            })
            .map(appConfigRepository::save)
            .map(appConfigMapper::toDto);
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
        return appConfigRepository.findAll(pageable).map(appConfigMapper::toDto);
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
        return appConfigRepository.findById(id).map(appConfigMapper::toDto);
    }

    /**
     * Delete the appConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AppConfig : {}", id);
        appConfigRepository.deleteById(id);
    }
}
