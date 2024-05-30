package devexperts.chatbackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "chats", schema = "public")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Message> history;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chats_users", schema = "public",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
