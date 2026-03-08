package rs.hexatech.beeback.web.rest;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.hexatech.beeback.exception.DeviceIdForbiddenException;
import rs.hexatech.beeback.service.ReminderService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.ReminderDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for reminders and FCM registration.
 */
@RestController
@RequestMapping("/api/reminders")
public class ReminderResource {

  private static final Logger LOG = LoggerFactory.getLogger(ReminderResource.class);
  private static final String ENTITY_NAME = "reminder";

  @Autowired
  SecurityService securityService;

  private final ReminderService reminderService;

  public ReminderResource(ReminderService reminderService) {
    this.reminderService = reminderService;
  }

  /**
   * POST /api/reminders/register-fcm : Register FCM token for the current device.
   * Body: { "fcmToken": "string" }. Header: Device-Id required.
   */
  @PostMapping("/register-fcm")
  public ResponseEntity<Void> registerFcm(@RequestHeader("Device-Id") String deviceId,
                                          @RequestBody Map<String, String> body) {
    securityService.checkUserDeviceId(deviceId);
    String fcmToken = body != null ? body.get("fcmToken") : null;
    if (fcmToken == null || fcmToken.isBlank()) {
      throw new BadRequestAlertException("fcmToken is required", ENTITY_NAME, "fcmtokenrequired");
    }
    reminderService.registerFcmToken(deviceId, fcmToken.trim());
    return ResponseEntity.noContent().build();
  }

  /**
   * POST /api/reminders : Create a reminder. Body: uuid, title, body, scheduledAt (ISO UTC), hiveId?, groupRecordId?.
   */
  @PostMapping("")
  public ResponseEntity<ReminderDTO> createReminder(@RequestHeader("Device-Id") String deviceId,
                                                     @Valid @RequestBody ReminderDTO dto) {
    securityService.checkUserDeviceId(deviceId);
    if (dto.getId() != null) {
      throw new BadRequestAlertException("A new reminder cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ReminderDTO created = reminderService.create(dto, deviceId);
    LOG.info("Reminder created: id={}, scheduledAt={}, title={}", created.getId(), created.getScheduledAt(), created.getTitle());
    return ResponseEntity.ok(created);
  }

  /**
   * PUT /api/reminders/:id : Update a reminder.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ReminderDTO> updateReminder(@RequestHeader("Device-Id") String deviceId,
                                                    @PathVariable Long id,
                                                    @Valid @RequestBody ReminderDTO dto) {
    securityService.checkUserDeviceId(deviceId);
    Optional<ReminderDTO> updated = reminderService.update(id, dto);
    return updated.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * DELETE /api/reminders/:id : Delete a reminder.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReminder(@RequestHeader("Device-Id") String deviceId,
                                             @PathVariable Long id) {
    securityService.checkUserDeviceId(deviceId);
    boolean deleted = reminderService.delete(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  /**
   * GET /api/reminders/all : Get all reminders for the current user.
   */
  @GetMapping("/all")
  public ResponseEntity<List<ReminderDTO>> getAllReminders(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    securityService.checkUserDeviceId(deviceId);
    List<ReminderDTO> list = reminderService.findAllForCurrentUser();
    return ResponseEntity.ok(list);
  }
}
