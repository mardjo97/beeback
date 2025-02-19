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
import rs.hexatech.beeback.repository.MovedHiveRepository;
import rs.hexatech.beeback.service.MovedHiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.MovedHive}.
 */
@RestController
@RequestMapping("/api/moved-hives")
public class MovedHiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(MovedHiveResource.class);

  private static final String ENTITY_NAME = "movedHive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final MovedHiveService service;

  @Autowired
  SecurityService securityService;

  public MovedHiveResource(MovedHiveService movedHiveService, MovedHiveRepository movedHiveRepository) {
    this.service = movedHiveService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<MovedHiveDTO>> syncEntities(@RequestBody List<MovedHiveDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<MovedHiveDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<MovedHiveDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<MovedHiveDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /moved-hives} : Create a new movedHive.
   *
   * @param movedHiveDTO the movedHiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movedHiveDTO, or with status {@code 400 (Bad Request)} if the movedHive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<MovedHiveDTO> createMovedHive(@Valid @RequestBody MovedHiveDTO movedHiveDTO) throws URISyntaxException {
    LOG.debug("REST request to save MovedHive : {}", movedHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (movedHiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new movedHive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    movedHiveDTO = service.save(movedHiveDTO);
//    return ResponseEntity.created(new URI("/api/moved-hives/" + movedHiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, movedHiveDTO.getId().toString()))
//        .body(movedHiveDTO);
  }

  /**
   * {@code PUT  /moved-hives/:id} : Updates an existing movedHive.
   *
   * @param id           the id of the movedHiveDTO to save.
   * @param movedHiveDTO the movedHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movedHiveDTO,
   * or with status {@code 400 (Bad Request)} if the movedHiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the movedHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<MovedHiveDTO> updateMovedHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody MovedHiveDTO movedHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update MovedHive : {}, {}", id, movedHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (movedHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, movedHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!movedHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    movedHiveDTO = service.update(movedHiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movedHiveDTO.getId().toString()))
//        .body(movedHiveDTO);
  }

  /**
   * {@code PATCH  /moved-hives/:id} : Partial updates given fields of an existing movedHive, field will ignore if it is null
   *
   * @param id           the id of the movedHiveDTO to save.
   * @param movedHiveDTO the movedHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movedHiveDTO,
   * or with status {@code 400 (Bad Request)} if the movedHiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the movedHiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the movedHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<MovedHiveDTO> partialUpdateMovedHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody MovedHiveDTO movedHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update MovedHive partially : {}, {}", id, movedHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (movedHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, movedHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!movedHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<MovedHiveDTO> result = service.partialUpdate(movedHiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movedHiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /moved-hives} : get all the movedHives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movedHives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<MovedHiveDTO>> getAllMovedHives(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of MovedHives");
    Page<MovedHiveDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /moved-hives/:id} : get the "id" movedHive.
   *
   * @param id the id of the movedHiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movedHiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<MovedHiveDTO> getMovedHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get MovedHive : {}", id);
    Optional<MovedHiveDTO> movedHiveDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(movedHiveDTO);
  }

  /**
   * {@code DELETE  /moved-hives/:id} : delete the "id" movedHive.
   *
   * @param id the id of the movedHiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovedHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete MovedHive : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
