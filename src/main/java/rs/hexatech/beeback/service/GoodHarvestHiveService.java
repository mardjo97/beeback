package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.repository.GoodHarvestHiveRepository;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;
import rs.hexatech.beeback.service.mapper.GoodHarvestHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.GoodHarvestHive}.
 */
@Service
@Transactional
public class GoodHarvestHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(GoodHarvestHiveService.class);

    private final GoodHarvestHiveRepository goodHarvestHiveRepository;

    private final GoodHarvestHiveMapper goodHarvestHiveMapper;

    public GoodHarvestHiveService(GoodHarvestHiveRepository goodHarvestHiveRepository, GoodHarvestHiveMapper goodHarvestHiveMapper) {
        this.goodHarvestHiveRepository = goodHarvestHiveRepository;
        this.goodHarvestHiveMapper = goodHarvestHiveMapper;
    }

    /**
     * Save a goodHarvestHive.
     *
     * @param goodHarvestHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public GoodHarvestHiveDTO save(GoodHarvestHiveDTO goodHarvestHiveDTO) {
        LOG.debug("Request to save GoodHarvestHive : {}", goodHarvestHiveDTO);
        GoodHarvestHive goodHarvestHive = goodHarvestHiveMapper.toEntity(goodHarvestHiveDTO);
        goodHarvestHive = goodHarvestHiveRepository.save(goodHarvestHive);
        return goodHarvestHiveMapper.toDto(goodHarvestHive);
    }

    /**
     * Update a goodHarvestHive.
     *
     * @param goodHarvestHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public GoodHarvestHiveDTO update(GoodHarvestHiveDTO goodHarvestHiveDTO) {
        LOG.debug("Request to update GoodHarvestHive : {}", goodHarvestHiveDTO);
        GoodHarvestHive goodHarvestHive = goodHarvestHiveMapper.toEntity(goodHarvestHiveDTO);
        goodHarvestHive = goodHarvestHiveRepository.save(goodHarvestHive);
        return goodHarvestHiveMapper.toDto(goodHarvestHive);
    }

    /**
     * Partially update a goodHarvestHive.
     *
     * @param goodHarvestHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GoodHarvestHiveDTO> partialUpdate(GoodHarvestHiveDTO goodHarvestHiveDTO) {
        LOG.debug("Request to partially update GoodHarvestHive : {}", goodHarvestHiveDTO);

        return goodHarvestHiveRepository
            .findById(goodHarvestHiveDTO.getId())
            .map(existingGoodHarvestHive -> {
                goodHarvestHiveMapper.partialUpdate(existingGoodHarvestHive, goodHarvestHiveDTO);

                return existingGoodHarvestHive;
            })
            .map(goodHarvestHiveRepository::save)
            .map(goodHarvestHiveMapper::toDto);
    }

    /**
     * Get all the goodHarvestHives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodHarvestHiveDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all GoodHarvestHives");
        return goodHarvestHiveRepository.findAll(pageable).map(goodHarvestHiveMapper::toDto);
    }

    /**
     * Get one goodHarvestHive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodHarvestHiveDTO> findOne(Long id) {
        LOG.debug("Request to get GoodHarvestHive : {}", id);
        return goodHarvestHiveRepository.findById(id).map(goodHarvestHiveMapper::toDto);
    }

    /**
     * Delete the goodHarvestHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GoodHarvestHive : {}", id);
        goodHarvestHiveRepository.deleteById(id);
    }
}
