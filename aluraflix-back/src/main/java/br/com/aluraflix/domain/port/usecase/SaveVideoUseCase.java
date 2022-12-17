package br.com.aluraflix.domain.port.usecase;

import br.com.aluraflix.domain.entity.Video;

public interface SaveVideoUseCase {

    Video save(Video video);

}
