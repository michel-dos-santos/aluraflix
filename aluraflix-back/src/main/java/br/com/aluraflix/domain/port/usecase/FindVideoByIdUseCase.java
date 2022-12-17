package br.com.aluraflix.domain.port.usecase;

import br.com.aluraflix.domain.entity.Video;

import java.util.UUID;

public interface FindVideoByIdUseCase {

    Video findById(UUID id);

}
