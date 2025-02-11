package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.repository.HarvestTypeRepository;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.mapper.HarvestTypeMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.HarvestType}.
 */
@Service
@Transactional
public class HarvestTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(HarvestTypeService.class);

    private final HarvestTypeRepository harvestTypeRepository;

    private final HarvestTypeMapper harvestTypeMapper;

    public HarvestTypeService(HarvestTypeRepository harvestTypeRepository, HarvestTypeMapper harvestTypeMapper) {
        this.harvestTypeRepository = harvestTypeRepository;
        this.harvestTypeMapper = harvestTypeMapper;
    }

    /**
     * Save a harvestType.
     *
     * @param harvestTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public HarvestTypeDTO save(HarvestTypeDTO harvestTypeDTO) {
        LOG.debug("Request to save HarvestType : {}", harvestTypeDTO);
        HarvestType harvestType = harvestTypeMapper.toEntity(harvestTypeDTO);
        harvestType = harvestTypeRepository.save(harvestType);
        return harvestTypeMapper.toDto(harvestType);
    }

    /**
     * Update a harvestType.
     *
     * @param harvestTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public HarvestTypeDTO update(HarvestTypeDTO harvestTypeDTO) {
        LOG.debug("Request to update HarvestType : {}", harvestTypeDTO);
        HarvestType harvestType = harvestTypeMapper.toEntity(harvestTypeDTO);
        harvestType = harvestTypeRepository.save(harvestType);
        return harvestTypeMapper.toDto(harvestType);
    }

    /**
     * Partially update a harvestType.
     *
     * @param harvestTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HarvestTypeDTO> partialUpdate(HarvestTypeDTO harvestTypeDTO) {
        LOG.debug("Request to partially update HarvestType : {}", harvestTypeDTO);

        return harvestTypeRepository
            .findById(harvestTypeDTO.getId())
            .map(existingHarvestType -> {
                harvestTypeMapper.partialUpdate(existingHarvestType, harvestTypeDTO);

                return existingHarvestType;
            })
            .map(harvestTypeRepository::save)
            .map(harvestTypeMapper::toDto);
    }

    /**
     * Get all the harvestTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HarvestTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HarvestTypes");
        return harvestTypeRepository.findAll(pageable).map(harvestTypeMapper::toDto);
    }

    /**
     * Get one harvestType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HarvestTypeDTO> findOne(Long id) {
        LOG.debug("Request to get HarvestType : {}", id);
        return harvestTypeRepository.findById(id).map(harvestTypeMapper::toDto);
    }

    /**
     * Delete the harvestType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HarvestType : {}", id);
        harvestTypeRepository.deleteById(id);
    }
}
