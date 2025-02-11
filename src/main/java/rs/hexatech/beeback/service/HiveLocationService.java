package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.repository.HiveLocationRepository;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;
import rs.hexatech.beeback.service.mapper.HiveLocationMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.HiveLocation}.
 */
@Service
@Transactional
public class HiveLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(HiveLocationService.class);

    private final HiveLocationRepository hiveLocationRepository;

    private final HiveLocationMapper hiveLocationMapper;

    public HiveLocationService(HiveLocationRepository hiveLocationRepository, HiveLocationMapper hiveLocationMapper) {
        this.hiveLocationRepository = hiveLocationRepository;
        this.hiveLocationMapper = hiveLocationMapper;
    }

    /**
     * Save a hiveLocation.
     *
     * @param hiveLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public HiveLocationDTO save(HiveLocationDTO hiveLocationDTO) {
        LOG.debug("Request to save HiveLocation : {}", hiveLocationDTO);
        HiveLocation hiveLocation = hiveLocationMapper.toEntity(hiveLocationDTO);
        hiveLocation = hiveLocationRepository.save(hiveLocation);
        return hiveLocationMapper.toDto(hiveLocation);
    }

    /**
     * Update a hiveLocation.
     *
     * @param hiveLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public HiveLocationDTO update(HiveLocationDTO hiveLocationDTO) {
        LOG.debug("Request to update HiveLocation : {}", hiveLocationDTO);
        HiveLocation hiveLocation = hiveLocationMapper.toEntity(hiveLocationDTO);
        hiveLocation = hiveLocationRepository.save(hiveLocation);
        return hiveLocationMapper.toDto(hiveLocation);
    }

    /**
     * Partially update a hiveLocation.
     *
     * @param hiveLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HiveLocationDTO> partialUpdate(HiveLocationDTO hiveLocationDTO) {
        LOG.debug("Request to partially update HiveLocation : {}", hiveLocationDTO);

        return hiveLocationRepository
            .findById(hiveLocationDTO.getId())
            .map(existingHiveLocation -> {
                hiveLocationMapper.partialUpdate(existingHiveLocation, hiveLocationDTO);

                return existingHiveLocation;
            })
            .map(hiveLocationRepository::save)
            .map(hiveLocationMapper::toDto);
    }

    /**
     * Get all the hiveLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HiveLocationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HiveLocations");
        return hiveLocationRepository.findAll(pageable).map(hiveLocationMapper::toDto);
    }

    /**
     * Get one hiveLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HiveLocationDTO> findOne(Long id) {
        LOG.debug("Request to get HiveLocation : {}", id);
        return hiveLocationRepository.findById(id).map(hiveLocationMapper::toDto);
    }

    /**
     * Delete the hiveLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HiveLocation : {}", id);
        hiveLocationRepository.deleteById(id);
    }
}
