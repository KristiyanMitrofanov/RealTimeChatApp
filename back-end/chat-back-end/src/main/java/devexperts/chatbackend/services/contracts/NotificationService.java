package devexperts.chatbackend.services.contracts;

import devexperts.chatbackend.models.Notification;
import reactor.core.publisher.Sinks;

public interface NotificationService {

    Sinks.Many<Notification> getNotifications(long chatId);

    void sendNotification(long chatId, Notification notification);
}
