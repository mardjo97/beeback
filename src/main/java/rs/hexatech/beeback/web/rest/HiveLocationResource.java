package rs.hexatech.beeback.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.hexatech.beeback.repository.HiveLocationRepository;
import rs.hexatech.beeback.service.HiveLocationService;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.HiveLocation}.
 */
@RestController
@RequestMapping("/api/hive-locations")
public class HiveLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(HiveLocationResource.class);

    private static final String ENTITY_NAME = "hiveLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HiveLocationService hiveLocationService;

    private final HiveLocationRepository hiveLocationRepository;

    public HiveLocationResource(HiveLocationService hiveLocationService, HiveLocationRepository hiveLocationRepository) {
        this.hiveLocationService = hiveLocationService;
        this.hiveLocationRepository = hiveLocationRepository;
    }

    /**
     * {@code POST  /hive-locations} : Create a new hiveLocation.
     *
     * @param hiveLocationDTO the hiveLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hiveLocationDTO, or with status {@code 400 (Bad Request)} if the hiveLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HiveLocationDTO> createHiveLocation(@Valid @RequestBody HiveLocationDTO hiveLocationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HiveLocation : {}", hiveLocationDTO);
        if (hiveLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new hiveLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hiveLocationDTO = hiveLocationService.save(hiveLocationDTO);
        return ResponseEntity.created(new URI("/api/hive-locations/" + hiveLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hiveLocationDTO.getId().toString()))
            .body(hiveLocationDTO);
    }

    /**
     * {@code PUT  /hive-locations/:id} : Updates an existing hiveLocation.
     *
     * @param id the id of the hiveLocationDTO to save.
     * @param hiveLocationDTO the hiveLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveLocationDTO,
     * or with status {@code 400 (Bad Request)} if the hiveLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hiveLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HiveLocationDTO> updateHiveLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HiveLocationDTO hiveLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HiveLocation : {}, {}", id, hiveLocationDTO);
        if (hiveLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hiveLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hiveLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hiveLocationDTO = hiveLocationService.update(hiveLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveLocationDTO.getId().toString()))
            .body(hiveLocationDTO);
    }

    /**
     * {@code PATCH  /hive-locations/:id} : Partial updates given fields of an existing hiveLocation, field will ignore if it is null
     *
     * @param id the id of the hiveLocationDTO to save.
     * @param hiveLocationDTO the hiveLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveLocationDTO,
     * or with status {@code 400 (Bad Request)} if the hiveLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hiveLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hiveLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HiveLocationDTO> partialUpdateHiveLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HiveLocationDTO hiveLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HiveLocation partially : {}, {}", id, hiveLocationDTO);
        if (hiveLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hiveLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hiveLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HiveLocationDTO> result = hiveLocationService.partialUpdate(hiveLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hive-locations} : get all the hiveLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hiveLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HiveLocationDTO>> getAllHiveLocations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of HiveLocations");
        Page<HiveLocationDTO> page = hiveLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hive-locations/:id} : get the "id" hiveLocation.
     *
     * @param id the id of the hiveLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hiveLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HiveLocationDTO> getHiveLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HiveLocation : {}", id);
        Optional<HiveLocationDTO> hiveLocationDTO = hiveLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hiveLocationDTO);
    }

    /**
     * {@code DELETE  /hive-locations/:id} : delete the "id" hiveLocation.
     *
     * @param id the id of the hiveLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHiveLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HiveLocation : {}", id);
        hiveLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
