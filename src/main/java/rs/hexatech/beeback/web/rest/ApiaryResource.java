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
import rs.hexatech.beeback.service.ApiaryService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.ApiaryDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.Apiary}.
 */
@RestController
@RequestMapping("/api/apiaries")
public class ApiaryResource {

  private static final Logger LOG = LoggerFactory.getLogger(ApiaryResource.class);

  private static final String ENTITY_NAME = "apiary";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ApiaryService apiaryService;

  @Autowired
  private SecurityService securityService;

  public ApiaryResource(ApiaryService apiaryService) {
    this.apiaryService = apiaryService;
  }

  /**
   * {@code POST  /apiaries/sync} : Sync list of apiaries.
   *
   * @param apiaryDTOs the apiaryDTO to create.
   * @return the {@link ResponseEntity} with status {@code 200 } and with List of the apiaryDTO, or with status {@code 400 (Bad Request)} if the apiary has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/sync")
  public ResponseEntity<List<ApiaryDTO>> syncApiaries(@RequestBody List<ApiaryDTO> apiaryDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Apiaries : {}", apiaryDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<ApiaryDTO> syncedApiariesDTOs = apiaryService.sync(apiaryDTOs);
    return ResponseEntity.ok().body(syncedApiariesDTOs);
  }

  /**
   * {@code GET  /apiaries/all} : Retrieve list of user's apiaries.
   *
   * @param deviceId to validate device id
   * @return the {@link ResponseEntity} with status {@code 200 } and with List of the apiaryDTO, or with status {@code 400 (Bad Request)} if the apiary has already an ID.
   */
  @GetMapping("/all")
  public ResponseEntity<List<ApiaryDTO>> userApiaries(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Apiaries");
    securityService.checkUserDeviceId(deviceId);
    List<ApiaryDTO> userApiaries = apiaryService.userApiaries(deviceId);
    return ResponseEntity.ok().body(userApiaries);
  }

  /**
   * {@code POST  /apiaries} : Create a new apiary.
   *
   * @param apiaryDTO the apiaryDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiaryDTO, or with status {@code 400 (Bad Request)} if the apiary has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<ApiaryDTO> createApiary(@Valid @RequestBody ApiaryDTO apiaryDTO) throws URISyntaxException {
    LOG.debug("REST request to save Apiary : {}", apiaryDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (apiaryDTO.getId() != null) {
//      throw new BadRequestAlertException("A new apiary cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    apiaryDTO = apiaryService.save(apiaryDTO);
//    return ResponseEntity.created(new URI("/api/apiaries/" + apiaryDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, apiaryDTO.getId().toString()))
//        .body(apiaryDTO);
  }

  /**
   * {@code PUT  /apiaries/:id} : Updates an existing apiary.
   *
   * @param id        the id of the apiaryDTO to save.
   * @param apiaryDTO the apiaryDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiaryDTO,
   * or with status {@code 400 (Bad Request)} if the apiaryDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the apiaryDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ApiaryDTO> updateApiary(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody ApiaryDTO apiaryDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update Apiary : {}, {}", id, apiaryDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (apiaryDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, apiaryDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!apiaryRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    apiaryDTO = apiaryService.update(apiaryDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiaryDTO.getId().toString()))
//        .body(apiaryDTO);
  }

  /**
   * {@code PATCH  /apiaries/:id} : Partial updates given fields of an existing apiary, field will ignore if it is null
   *
   * @param id        the id of the apiaryDTO to save.
   * @param apiaryDTO the apiaryDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiaryDTO,
   * or with status {@code 400 (Bad Request)} if the apiaryDTO is not valid,
   * or with status {@code 404 (Not Found)} if the apiaryDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the apiaryDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<ApiaryDTO> partialUpdateApiary(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody ApiaryDTO apiaryDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update Apiary partially : {}, {}", id, apiaryDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (apiaryDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, apiaryDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!apiaryRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<ApiaryDTO> result = apiaryService.partialUpdate(apiaryDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiaryDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /apiaries} : get all the apiaries.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiaries in body.
   */
  @GetMapping("")
  public ResponseEntity<List<ApiaryDTO>> getAllApiaries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of Apiaries");
    Page<ApiaryDTO> page = apiaryService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /apiaries/:id} : get the "id" apiary.
   *
   * @param id the id of the apiaryDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiaryDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ApiaryDTO> getApiary(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Apiary : {}", id);
    Optional<ApiaryDTO> apiaryDTO = apiaryService.findOne(id);
    return ResponseUtil.wrapOrNotFound(apiaryDTO);
  }

  /**
   * {@code DELETE  /apiaries/:id} : delete the "id" apiary.
   *
   * @param id the id of the apiaryDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteApiary(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete Apiary : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    apiaryService.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
