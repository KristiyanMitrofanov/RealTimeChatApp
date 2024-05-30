package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.models.Notification;
import devexperts.chatbackend.services.contracts.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping(value = "/notifications/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> getNotifications(@PathVariable long chatId) {
        return service.getNotifications(chatId).asFlux();
    }
}
