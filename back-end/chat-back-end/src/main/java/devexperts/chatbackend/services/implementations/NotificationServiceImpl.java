package devexperts.chatbackend.services.implementations;

import devexperts.chatbackend.models.Notification;
import devexperts.chatbackend.services.contracts.NotificationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final ConcurrentMap<Long, Sinks.Many<Notification>> sinksMap = new ConcurrentHashMap<>();

    @Override
    public Sinks.Many<Notification> getNotifications(long chatId) {
        return sinksMap.computeIfAbsent(chatId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    @Override
    public void sendNotification(long chatId, Notification notification) {
        Sinks.Many<Notification> sink = sinksMap.get(chatId);
        if (sink != null) {
            sink.tryEmitNext(notification);
        }
    }
}


