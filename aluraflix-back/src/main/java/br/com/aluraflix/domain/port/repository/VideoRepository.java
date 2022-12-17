package br.com.aluraflix.domain.port.repository;

import br.com.aluraflix.domain.entity.Video;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository {

    Video save(Video video);
    List<Video> findAll();
    Optional<Video> findById(UUID id);
    void deleteById(UUID id);

}
