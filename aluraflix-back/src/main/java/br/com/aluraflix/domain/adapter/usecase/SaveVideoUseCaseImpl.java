package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.SaveVideoUseCase;

public class SaveVideoUseCaseImpl implements SaveVideoUseCase {

    private final VideoRepository videoRepository;
    private final LogRepository logRepository;

    public SaveVideoUseCaseImpl(VideoRepository videoRepository, LogRepository logRepository) {
        this.videoRepository = videoRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Video save(Video video) {
        logRepository.debug(String.format("Caso de Uso: saveVideoUseCase -> Iniciando o processo de adição dos dados do video: %s", video));
        videoRepository.save(video);
        logRepository.debug(String.format("Caso de Uso: saveVideoUseCase -> Finalizando o processo de adição dos dados do video: %s", video));
        return video;
    }
}
