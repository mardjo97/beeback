package rs.hexatech.beeback.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Sends FCM notifications. If application.firebase.credentials-path is not set or invalid, send is a no-op.
 */
@Service
public class FcmService {

  private static final Logger LOG = LoggerFactory.getLogger(FcmService.class);

  @Value("${application.firebase.credentials-path:}")
  private String credentialsPath;

  private boolean initialized = false;

  @PostConstruct
  public void init() {
    if (credentialsPath == null || credentialsPath.isBlank()) {
      LOG.info("FCM: no credentials path set; reminder push notifications disabled.");
      return;
    }
    try (FileInputStream fis = new FileInputStream(credentialsPath.trim())) {
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(fis))
          .build();
      FirebaseApp.initializeApp(options);
      initialized = true;
      LOG.info("FCM: initialized from {}", credentialsPath);
    } catch (IOException e) {
      LOG.warn("FCM: could not load credentials from {}: {}", credentialsPath, e.getMessage());
    } catch (Exception e) {
      LOG.warn("FCM: initialization failed: {}", e.getMessage());
    }
  }

  /**
   * Send a notification to the given FCM token. No-op if FCM is not initialized.
   *
   * @return true if sent successfully, false if skipped or failed.
   */
  public boolean sendToToken(String fcmToken, String title, String body) {
    if (!initialized || fcmToken == null || fcmToken.isBlank()) {
      return false;
    }
    try {
      Notification notification = Notification.builder()
          .setTitle(title != null ? title : "")
          .setBody(body != null ? body : "")
          .build();
      Message message = Message.builder()
          .setToken(fcmToken.trim())
          .setNotification(notification)
          .build();
      String messageId = FirebaseMessaging.getInstance().send(message);
      LOG.debug("FCM sent: {}", messageId);
      return true;
    } catch (FirebaseMessagingException e) {
      LOG.warn("FCM send failed for token...{}: {}", fcmToken.substring(Math.max(0, fcmToken.length() - 8)), e.getMessage());
      return false;
    }
  }

  public boolean isInitialized() {
    return initialized;
  }
}
