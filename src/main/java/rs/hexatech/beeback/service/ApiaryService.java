package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.repository.ApiaryRepository;
import rs.hexatech.beeback.service.dto.ApiaryDTO;
import rs.hexatech.beeback.service.mapper.ApiaryMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Apiary}.
 */
@Service
@Transactional
public class ApiaryService {

    private static final Logger LOG = LoggerFactory.getLogger(ApiaryService.class);

    private final ApiaryRepository apiaryRepository;

    private final ApiaryMapper apiaryMapper;

    public ApiaryService(ApiaryRepository apiaryRepository, ApiaryMapper apiaryMapper) {
        this.apiaryRepository = apiaryRepository;
        this.apiaryMapper = apiaryMapper;
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
        LOG.debug("Request to get all Apiaries");
        return apiaryRepository.findAll(pageable).map(apiaryMapper::toDto);
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
}
