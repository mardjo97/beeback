package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Reminder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Sends due reminders via FCM and removes them.
 */
@Component
public class ReminderScheduler {

  private static final Logger LOG = LoggerFactory.getLogger(ReminderScheduler.class);

  private final ReminderService reminderService;
  private final FcmService fcmService;

  public ReminderScheduler(ReminderService reminderService, FcmService fcmService) {
    this.reminderService = reminderService;
    this.fcmService = fcmService;
  }

  @Scheduled(fixedDelayString = "${application.reminder-scheduler.interval-ms:60000}")
  @Transactional
  public void sendDueReminders() {
    if (!fcmService.isInitialized()) {
          LOG.warn("Reminder job ran: FCM service not initialized.");
      return;
    }
    Instant now = Instant.now();
    List<Reminder> due = reminderService.findDueReminders(now);
    LOG.info("Reminder job ran: {} due reminder(s) at {}", due.size(), now);
    for (Reminder r : due) {
      try {
        String title = r.getTitle() != null ? r.getTitle() : "Podsetnik";
        String body = r.getBody() != null ? r.getBody() : "";
        Optional<String> tokenOpt = reminderService.getFcmTokenForDevice(r.getDeviceId());
        if (tokenOpt.isEmpty()) {
          LOG.warn("Reminder {}: no FCM token for device {}", r.getId(), r.getDeviceId());
        } else {
          String token = tokenOpt.orElseThrow();
          boolean sent = fcmService.sendToToken(token, title, body);
          if (sent) {
            LOG.info("Reminder {} sent via FCM to device {}", r.getId(), r.getDeviceId());
          } else {
            LOG.warn("Reminder {}: FCM send failed for device {}", r.getId(), r.getDeviceId());
          }
        }
      } catch (Exception e) {
        LOG.warn("Reminder {} send error: {}", r.getId(), e.getMessage());
      } finally {
        reminderService.deleteReminder(r);
      }
    }
  }
}
