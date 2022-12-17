package br.com.aluraflix.infrastructure.repository.postgres.repository;

import br.com.aluraflix.infrastructure.repository.postgres.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPostgresVideoRepository extends JpaRepository<VideoEntity, UUID> {

}
