package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Harvest;
import rs.hexatech.beeback.repository.HarvestRepository;
import rs.hexatech.beeback.service.dto.HarvestDTO;
import rs.hexatech.beeback.service.mapper.HarvestMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Harvest}.
 */
@Service
@Transactional
public class HarvestService {

    private static final Logger LOG = LoggerFactory.getLogger(HarvestService.class);

    private final HarvestRepository harvestRepository;

    private final HarvestMapper harvestMapper;

    public HarvestService(HarvestRepository harvestRepository, HarvestMapper harvestMapper) {
        this.harvestRepository = harvestRepository;
        this.harvestMapper = harvestMapper;
    }

    /**
     * Save a harvest.
     *
     * @param harvestDTO the entity to save.
     * @return the persisted entity.
     */
    public HarvestDTO save(HarvestDTO harvestDTO) {
        LOG.debug("Request to save Harvest : {}", harvestDTO);
        Harvest harvest = harvestMapper.toEntity(harvestDTO);
        harvest = harvestRepository.save(harvest);
        return harvestMapper.toDto(harvest);
    }

    /**
     * Update a harvest.
     *
     * @param harvestDTO the entity to save.
     * @return the persisted entity.
     */
    public HarvestDTO update(HarvestDTO harvestDTO) {
        LOG.debug("Request to update Harvest : {}", harvestDTO);
        Harvest harvest = harvestMapper.toEntity(harvestDTO);
        harvest = harvestRepository.save(harvest);
        return harvestMapper.toDto(harvest);
    }

    /**
     * Partially update a harvest.
     *
     * @param harvestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HarvestDTO> partialUpdate(HarvestDTO harvestDTO) {
        LOG.debug("Request to partially update Harvest : {}", harvestDTO);

        return harvestRepository
            .findById(harvestDTO.getId())
            .map(existingHarvest -> {
                harvestMapper.partialUpdate(existingHarvest, harvestDTO);

                return existingHarvest;
            })
            .map(harvestRepository::save)
            .map(harvestMapper::toDto);
    }

    /**
     * Get all the harvests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HarvestDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Harvests");
        return harvestRepository.findAll(pageable).map(harvestMapper::toDto);
    }

    /**
     * Get one harvest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HarvestDTO> findOne(Long id) {
        LOG.debug("Request to get Harvest : {}", id);
        return harvestRepository.findById(id).map(harvestMapper::toDto);
    }

    /**
     * Delete the harvest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Harvest : {}", id);
        harvestRepository.deleteById(id);
    }
}
