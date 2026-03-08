package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.DeviceFcmToken;
import rs.hexatech.beeback.domain.Reminder;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.DeviceFcmTokenRepository;
import rs.hexatech.beeback.repository.HiveRepository;
import rs.hexatech.beeback.repository.ReminderRepository;
import rs.hexatech.beeback.service.dto.ReminderDTO;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReminderService {

  private static final Logger LOG = LoggerFactory.getLogger(ReminderService.class);

  private final ReminderRepository reminderRepository;
  private final DeviceFcmTokenRepository deviceFcmTokenRepository;
  private final HiveRepository hiveRepository;

  @Autowired
  SecurityService securityService;

  public ReminderService(ReminderRepository reminderRepository,
                         DeviceFcmTokenRepository deviceFcmTokenRepository,
                         HiveRepository hiveRepository) {
    this.reminderRepository = reminderRepository;
    this.deviceFcmTokenRepository = deviceFcmTokenRepository;
    this.hiveRepository = hiveRepository;
  }

  public void registerFcmToken(String deviceId, String fcmToken) {
    Instant now = DateTimeUtil.now();
    Optional<DeviceFcmToken> existing = deviceFcmTokenRepository.findById(deviceId);
    DeviceFcmToken token = existing.orElseGet(DeviceFcmToken::new);
    if (existing.isEmpty()) {
      token.setDeviceId(deviceId);
    }
    token.setFcmToken(fcmToken);
    token.setUpdatedAt(now);
    deviceFcmTokenRepository.save(token);
    LOG.debug("Registered FCM token for device: {}", deviceId);
  }

  public ReminderDTO create(ReminderDTO dto, String deviceId) {
    User user = securityService.getCurrentUser();
    Reminder r = new Reminder();
    r.setUser(user);
    r.setDeviceId(deviceId);
    r.setUuid(dto.getUuid());
    r.setTitle(dto.getTitle());
    r.setBody(dto.getBody());
    r.setScheduledAt(dto.getScheduledAt());
    r.setGroupRecordId(dto.getGroupRecordId());
    r.setCreatedAt(DateTimeUtil.now());
    r.setUpdatedAt(DateTimeUtil.now());
    if (dto.getHiveId() != null) {
      hiveRepository.findById(dto.getHiveId()).ifPresent(r::setHive);
    }
    r = reminderRepository.save(r);
    return toDto(r);
  }

  public Optional<ReminderDTO> update(Long id, ReminderDTO dto) {
    User user = securityService.getCurrentUser();
    return reminderRepository.findByIdAndUser(id, user)
        .map(r -> {
          r.setTitle(dto.getTitle());
          r.setBody(dto.getBody());
          r.setScheduledAt(dto.getScheduledAt());
          r.setGroupRecordId(dto.getGroupRecordId());
          r.setUpdatedAt(DateTimeUtil.now());
          if (dto.getHiveId() != null) {
            hiveRepository.findById(dto.getHiveId()).ifPresent(r::setHive);
          } else {
            r.setHive(null);
          }
          return reminderRepository.save(r);
        })
        .map(this::toDto);
  }

  public boolean delete(Long id) {
    User user = securityService.getCurrentUser();
    return reminderRepository.findByIdAndUser(id, user)
        .map(r -> {
          reminderRepository.delete(r);
          return true;
        })
        .orElse(false);
  }

  @Transactional(readOnly = true)
  public List<ReminderDTO> findAllForCurrentUser() {
    return reminderRepository.findByUserIsCurrentUser().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  /** Used by scheduler: find due reminders (scheduled_at <= now). */
  @Transactional(readOnly = true)
  public List<Reminder> findDueReminders(Instant now) {
    return reminderRepository.findByScheduledAtBefore(now);
  }

  public void deleteReminder(Reminder reminder) {
    reminderRepository.delete(reminder);
  }

  public Optional<String> getFcmTokenForDevice(String deviceId) {
    return deviceFcmTokenRepository.findById(deviceId)
        .map(DeviceFcmToken::getFcmToken);
  }

  private ReminderDTO toDto(Reminder r) {
    ReminderDTO dto = new ReminderDTO();
    dto.setId(r.getId());
    dto.setUuid(r.getUuid());
    dto.setTitle(r.getTitle());
    dto.setBody(r.getBody());
    dto.setScheduledAt(r.getScheduledAt());
    dto.setGroupRecordId(r.getGroupRecordId());
    if (r.getHive() != null) {
      dto.setHiveId(r.getHive().getId());
    }
    return dto;
  }
}
