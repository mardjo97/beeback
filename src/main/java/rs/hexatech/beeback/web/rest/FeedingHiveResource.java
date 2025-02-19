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
import rs.hexatech.beeback.service.FeedingHiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.FeedingHive}.
 */
@RestController
@RequestMapping("/api/feeding-hives")
public class FeedingHiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(FeedingHiveResource.class);

  private static final String ENTITY_NAME = "feedingHive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final FeedingHiveService service;

  @Autowired
  SecurityService securityService;

  public FeedingHiveResource(FeedingHiveService feedingHiveService) {
    this.service = feedingHiveService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<FeedingHiveDTO>> syncEntities(@RequestBody List<FeedingHiveDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<FeedingHiveDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<FeedingHiveDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<FeedingHiveDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /feeding-hives} : Create a new feedingHive.
   *
   * @param feedingHiveDTO the feedingHiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedingHiveDTO, or with status {@code 400 (Bad Request)} if the feedingHive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<FeedingHiveDTO> createFeedingHive(@Valid @RequestBody FeedingHiveDTO feedingHiveDTO) throws URISyntaxException {
    LOG.debug("REST request to save FeedingHive : {}", feedingHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (feedingHiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new feedingHive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    feedingHiveDTO = service.save(feedingHiveDTO);
//    return ResponseEntity.created(new URI("/api/feeding-hives/" + feedingHiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedingHiveDTO.getId().toString()))
//        .body(feedingHiveDTO);
  }

  /**
   * {@code PUT  /feeding-hives/:id} : Updates an existing feedingHive.
   *
   * @param id             the id of the feedingHiveDTO to save.
   * @param feedingHiveDTO the feedingHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedingHiveDTO,
   * or with status {@code 400 (Bad Request)} if the feedingHiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the feedingHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<FeedingHiveDTO> updateFeedingHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody FeedingHiveDTO feedingHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update FeedingHive : {}, {}", id, feedingHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (feedingHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, feedingHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!feedingHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    feedingHiveDTO = service.update(feedingHiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedingHiveDTO.getId().toString()))
//        .body(feedingHiveDTO);
  }

  /**
   * {@code PATCH  /feeding-hives/:id} : Partial updates given fields of an existing feedingHive, field will ignore if it is null
   *
   * @param id             the id of the feedingHiveDTO to save.
   * @param feedingHiveDTO the feedingHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedingHiveDTO,
   * or with status {@code 400 (Bad Request)} if the feedingHiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the feedingHiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the feedingHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<FeedingHiveDTO> partialUpdateFeedingHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody FeedingHiveDTO feedingHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update FeedingHive partially : {}, {}", id, feedingHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (feedingHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, feedingHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!feedingHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<FeedingHiveDTO> result = service.partialUpdate(feedingHiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedingHiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /feeding-hives} : get all the feedingHives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedingHives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<FeedingHiveDTO>> getAllFeedingHives(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of FeedingHives");
    Page<FeedingHiveDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /feeding-hives/:id} : get the "id" feedingHive.
   *
   * @param id the id of the feedingHiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedingHiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FeedingHiveDTO> getFeedingHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get FeedingHive : {}", id);
    Optional<FeedingHiveDTO> feedingHiveDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(feedingHiveDTO);
  }

  /**
   * {@code DELETE  /feeding-hives/:id} : delete the "id" feedingHive.
   *
   * @param id the id of the feedingHiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFeedingHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete FeedingHive : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
