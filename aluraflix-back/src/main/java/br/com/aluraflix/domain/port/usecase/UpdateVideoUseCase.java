package br.com.aluraflix.domain.port.usecase;

import br.com.aluraflix.domain.entity.Video;

import java.util.UUID;

public interface UpdateVideoUseCase {

    Video update(Video video);
    Video updateTitle(UUID id, String title);
    Video updateDescription(UUID id, String description);
    Video updateUrl(UUID id, String url);

}
