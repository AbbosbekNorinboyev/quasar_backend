package uz.brbtech.quasar_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.brbtech.quasar_backend.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}