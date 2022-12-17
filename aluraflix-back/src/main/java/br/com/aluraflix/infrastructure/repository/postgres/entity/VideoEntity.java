package br.com.aluraflix.infrastructure.repository.postgres.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "video")
@EntityListeners(AuditingEntityListener.class)
public class VideoEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String title;
    private String description;
    private String url;

}
