package pe.edu.upc.center.agecare.nutrition.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceClient {

    private final RestClient restClient;

    @Value("${services.notifications.url}")
    private String notificationServiceUrl;

    public NotificationServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean sendNotification(Long userId, String message) {
        try {
            String url = notificationServiceUrl + "/api/v1/notifications";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("type", "EMAIL");
            requestBody.put("message", message);
            requestBody.put("sentDate", LocalDate.now().toString());

            restClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();

            System.out.println("✅ Notification sent successfully to user: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error sending notification: " + e.getMessage());
            return false;
        }
    }
}
