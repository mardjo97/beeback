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
import rs.hexatech.beeback.repository.HarvestRepository;
import rs.hexatech.beeback.service.HarvestService;
import rs.hexatech.beeback.service.dto.HarvestDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.Harvest}.
 */
@RestController
@RequestMapping("/api/harvests")
public class HarvestResource {

    private static final Logger LOG = LoggerFactory.getLogger(HarvestResource.class);

    private static final String ENTITY_NAME = "harvest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HarvestService harvestService;

    private final HarvestRepository harvestRepository;

    public HarvestResource(HarvestService harvestService, HarvestRepository harvestRepository) {
        this.harvestService = harvestService;
        this.harvestRepository = harvestRepository;
    }

    /**
     * {@code POST  /harvests} : Create a new harvest.
     *
     * @param harvestDTO the harvestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new harvestDTO, or with status {@code 400 (Bad Request)} if the harvest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HarvestDTO> createHarvest(@Valid @RequestBody HarvestDTO harvestDTO) throws URISyntaxException {
        LOG.debug("REST request to save Harvest : {}", harvestDTO);
        if (harvestDTO.getId() != null) {
            throw new BadRequestAlertException("A new harvest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        harvestDTO = harvestService.save(harvestDTO);
        return ResponseEntity.created(new URI("/api/harvests/" + harvestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, harvestDTO.getId().toString()))
            .body(harvestDTO);
    }

    /**
     * {@code PUT  /harvests/:id} : Updates an existing harvest.
     *
     * @param id the id of the harvestDTO to save.
     * @param harvestDTO the harvestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harvestDTO,
     * or with status {@code 400 (Bad Request)} if the harvestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the harvestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HarvestDTO> updateHarvest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HarvestDTO harvestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Harvest : {}, {}", id, harvestDTO);
        if (harvestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harvestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harvestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        harvestDTO = harvestService.update(harvestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestDTO.getId().toString()))
            .body(harvestDTO);
    }

    /**
     * {@code PATCH  /harvests/:id} : Partial updates given fields of an existing harvest, field will ignore if it is null
     *
     * @param id the id of the harvestDTO to save.
     * @param harvestDTO the harvestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harvestDTO,
     * or with status {@code 400 (Bad Request)} if the harvestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the harvestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the harvestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HarvestDTO> partialUpdateHarvest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HarvestDTO harvestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Harvest partially : {}, {}", id, harvestDTO);
        if (harvestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harvestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harvestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HarvestDTO> result = harvestService.partialUpdate(harvestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /harvests} : get all the harvests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of harvests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HarvestDTO>> getAllHarvests(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Harvests");
        Page<HarvestDTO> page = harvestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /harvests/:id} : get the "id" harvest.
     *
     * @param id the id of the harvestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the harvestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HarvestDTO> getHarvest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Harvest : {}", id);
        Optional<HarvestDTO> harvestDTO = harvestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(harvestDTO);
    }

    /**
     * {@code DELETE  /harvests/:id} : delete the "id" harvest.
     *
     * @param id the id of the harvestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Harvest : {}", id);
        harvestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
