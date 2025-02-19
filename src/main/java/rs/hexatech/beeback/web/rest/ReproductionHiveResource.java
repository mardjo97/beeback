package rs.hexatech.beeback.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.hexatech.beeback.exception.DeviceIdForbiddenException;
import rs.hexatech.beeback.service.ReproductionHiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.ReproductionHive}.
 */
@RestController
@RequestMapping("/api/reproduction-hives")
public class ReproductionHiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(ReproductionHiveResource.class);

  private static final String ENTITY_NAME = "reproductionHive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ReproductionHiveService service;

  @Autowired
  SecurityService securityService;

  public ReproductionHiveResource(
      ReproductionHiveService reproductionHiveService
  ) {
    this.service = reproductionHiveService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<ReproductionHiveDTO>> syncEntities(@RequestBody List<ReproductionHiveDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<ReproductionHiveDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<ReproductionHiveDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<ReproductionHiveDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /reproduction-hives} : Create a new reproductionHive.
   *
   * @param reproductionHiveDTO the reproductionHiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reproductionHiveDTO, or with status {@code 400 (Bad Request)} if the reproductionHive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<ReproductionHiveDTO> createReproductionHive(@Valid @RequestBody ReproductionHiveDTO reproductionHiveDTO)
      throws URISyntaxException {
    LOG.debug("REST request to save ReproductionHive : {}", reproductionHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (reproductionHiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new reproductionHive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    reproductionHiveDTO = service.save(reproductionHiveDTO);
//    return ResponseEntity.created(new URI("/api/reproduction-hives/" + reproductionHiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reproductionHiveDTO.getId().toString()))
//        .body(reproductionHiveDTO);
  }

  /**
   * {@code PUT  /reproduction-hives/:id} : Updates an existing reproductionHive.
   *
   * @param id                  the id of the reproductionHiveDTO to save.
   * @param reproductionHiveDTO the reproductionHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reproductionHiveDTO,
   * or with status {@code 400 (Bad Request)} if the reproductionHiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the reproductionHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ReproductionHiveDTO> updateReproductionHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody ReproductionHiveDTO reproductionHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update ReproductionHive : {}, {}", id, reproductionHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (reproductionHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, reproductionHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!reproductionHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    reproductionHiveDTO = service.update(reproductionHiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reproductionHiveDTO.getId().toString()))
//        .body(reproductionHiveDTO);
  }

  /**
   * {@code PATCH  /reproduction-hives/:id} : Partial updates given fields of an existing reproductionHive, field will ignore if it is null
   *
   * @param id                  the id of the reproductionHiveDTO to save.
   * @param reproductionHiveDTO the reproductionHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reproductionHiveDTO,
   * or with status {@code 400 (Bad Request)} if the reproductionHiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the reproductionHiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the reproductionHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<ReproductionHiveDTO> partialUpdateReproductionHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody ReproductionHiveDTO reproductionHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update ReproductionHive partially : {}, {}", id, reproductionHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (reproductionHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, reproductionHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!reproductionHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<ReproductionHiveDTO> result = service.partialUpdate(reproductionHiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reproductionHiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /reproduction-hives} : get all the reproductionHives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reproductionHives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<ReproductionHiveDTO>> getAllReproductionHives(
      @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    LOG.debug("REST request to get a page of ReproductionHives");
    Page<ReproductionHiveDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /reproduction-hives/:id} : get the "id" reproductionHive.
   *
   * @param id the id of the reproductionHiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reproductionHiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ReproductionHiveDTO> getReproductionHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get ReproductionHive : {}", id);
    Optional<ReproductionHiveDTO> reproductionHiveDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(reproductionHiveDTO);
  }

  /**
   * {@code DELETE  /reproduction-hives/:id} : delete the "id" reproductionHive.
   *
   * @param id the id of the reproductionHiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReproductionHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete ReproductionHive : {}", id);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
