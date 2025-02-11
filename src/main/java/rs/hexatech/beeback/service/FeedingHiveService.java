package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.repository.FeedingHiveRepository;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;
import rs.hexatech.beeback.service.mapper.FeedingHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.FeedingHive}.
 */
@Service
@Transactional
public class FeedingHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedingHiveService.class);

    private final FeedingHiveRepository feedingHiveRepository;

    private final FeedingHiveMapper feedingHiveMapper;

    public FeedingHiveService(FeedingHiveRepository feedingHiveRepository, FeedingHiveMapper feedingHiveMapper) {
        this.feedingHiveRepository = feedingHiveRepository;
        this.feedingHiveMapper = feedingHiveMapper;
    }

    /**
     * Save a feedingHive.
     *
     * @param feedingHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedingHiveDTO save(FeedingHiveDTO feedingHiveDTO) {
        LOG.debug("Request to save FeedingHive : {}", feedingHiveDTO);
        FeedingHive feedingHive = feedingHiveMapper.toEntity(feedingHiveDTO);
        feedingHive = feedingHiveRepository.save(feedingHive);
        return feedingHiveMapper.toDto(feedingHive);
    }

    /**
     * Update a feedingHive.
     *
     * @param feedingHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedingHiveDTO update(FeedingHiveDTO feedingHiveDTO) {
        LOG.debug("Request to update FeedingHive : {}", feedingHiveDTO);
        FeedingHive feedingHive = feedingHiveMapper.toEntity(feedingHiveDTO);
        feedingHive = feedingHiveRepository.save(feedingHive);
        return feedingHiveMapper.toDto(feedingHive);
    }

    /**
     * Partially update a feedingHive.
     *
     * @param feedingHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeedingHiveDTO> partialUpdate(FeedingHiveDTO feedingHiveDTO) {
        LOG.debug("Request to partially update FeedingHive : {}", feedingHiveDTO);

        return feedingHiveRepository
            .findById(feedingHiveDTO.getId())
            .map(existingFeedingHive -> {
                feedingHiveMapper.partialUpdate(existingFeedingHive, feedingHiveDTO);

                return existingFeedingHive;
            })
            .map(feedingHiveRepository::save)
            .map(feedingHiveMapper::toDto);
    }

    /**
     * Get all the feedingHives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedingHiveDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FeedingHives");
        return feedingHiveRepository.findAll(pageable).map(feedingHiveMapper::toDto);
    }

    /**
     * Get one feedingHive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeedingHiveDTO> findOne(Long id) {
        LOG.debug("Request to get FeedingHive : {}", id);
        return feedingHiveRepository.findById(id).map(feedingHiveMapper::toDto);
    }

    /**
     * Delete the feedingHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FeedingHive : {}", id);
        feedingHiveRepository.deleteById(id);
    }
}
