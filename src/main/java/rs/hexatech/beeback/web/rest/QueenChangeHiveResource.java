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
import rs.hexatech.beeback.service.QueenChangeHiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.QueenChangeHive}.
 */
@RestController
@RequestMapping("/api/queen-change-hives")
public class QueenChangeHiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(QueenChangeHiveResource.class);

  private static final String ENTITY_NAME = "queenChangeHive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final QueenChangeHiveService service;

  @Autowired
  SecurityService securityService;

  public QueenChangeHiveResource(QueenChangeHiveService queenChangeHiveService) {
    this.service = queenChangeHiveService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<QueenChangeHiveDTO>> syncEntities(@RequestBody List<QueenChangeHiveDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<QueenChangeHiveDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<QueenChangeHiveDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<QueenChangeHiveDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /queen-change-hives} : Create a new queenChangeHive.
   *
   * @param queenChangeHiveDTO the queenChangeHiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queenChangeHiveDTO, or with status {@code 400 (Bad Request)} if the queenChangeHive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<QueenChangeHiveDTO> createQueenChangeHive(@Valid @RequestBody QueenChangeHiveDTO queenChangeHiveDTO)
      throws URISyntaxException {
    LOG.debug("REST request to save QueenChangeHive : {}", queenChangeHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenChangeHiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new queenChangeHive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    queenChangeHiveDTO = service.save(queenChangeHiveDTO);
//    return ResponseEntity.created(new URI("/api/queen-change-hives/" + queenChangeHiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, queenChangeHiveDTO.getId().toString()))
//        .body(queenChangeHiveDTO);
  }

  /**
   * {@code PUT  /queen-change-hives/:id} : Updates an existing queenChangeHive.
   *
   * @param id                 the id of the queenChangeHiveDTO to save.
   * @param queenChangeHiveDTO the queenChangeHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queenChangeHiveDTO,
   * or with status {@code 400 (Bad Request)} if the queenChangeHiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the queenChangeHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<QueenChangeHiveDTO> updateQueenChangeHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody QueenChangeHiveDTO queenChangeHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update QueenChangeHive : {}, {}", id, queenChangeHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenChangeHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, queenChangeHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!queenChangeHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    queenChangeHiveDTO = service.update(queenChangeHiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queenChangeHiveDTO.getId().toString()))
//        .body(queenChangeHiveDTO);
  }

  /**
   * {@code PATCH  /queen-change-hives/:id} : Partial updates given fields of an existing queenChangeHive, field will ignore if it is null
   *
   * @param id                 the id of the queenChangeHiveDTO to save.
   * @param queenChangeHiveDTO the queenChangeHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queenChangeHiveDTO,
   * or with status {@code 400 (Bad Request)} if the queenChangeHiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the queenChangeHiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the queenChangeHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<QueenChangeHiveDTO> partialUpdateQueenChangeHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody QueenChangeHiveDTO queenChangeHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update QueenChangeHive partially : {}, {}", id, queenChangeHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenChangeHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, queenChangeHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!queenChangeHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<QueenChangeHiveDTO> result = service.partialUpdate(queenChangeHiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queenChangeHiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /queen-change-hives} : get all the queenChangeHives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queenChangeHives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<QueenChangeHiveDTO>> getAllQueenChangeHives(
      @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    LOG.debug("REST request to get a page of QueenChangeHives");
    Page<QueenChangeHiveDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /queen-change-hives/:id} : get the "id" queenChangeHive.
   *
   * @param id the id of the queenChangeHiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queenChangeHiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<QueenChangeHiveDTO> getQueenChangeHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get QueenChangeHive : {}", id);
    Optional<QueenChangeHiveDTO> queenChangeHiveDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(queenChangeHiveDTO);
  }

  /**
   * {@code DELETE  /queen-change-hives/:id} : delete the "id" queenChangeHive.
   *
   * @param id the id of the queenChangeHiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQueenChangeHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete QueenChangeHive : {}", id);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
