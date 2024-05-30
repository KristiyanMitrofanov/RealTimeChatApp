package devexperts.chatbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Notification {

    private String sender;

    private String message;

    private long chatId;
}
