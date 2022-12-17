package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.FindVideoByIdUseCase;

import java.util.UUID;

public class FindVideoByIdUseCaseImpl implements FindVideoByIdUseCase {

    private final VideoRepository videoRepository;
    private final LogRepository logRepository;

    public FindVideoByIdUseCaseImpl(VideoRepository videoRepository, LogRepository logRepository) {
        this.videoRepository = videoRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Video findById(UUID id) {
        logRepository.debug(String.format("Caso de Uso: findVideoByIdUseCase -> Iniciando o processo de busca do video pelo id: %s", id));
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException("id", id.toString()));
        logRepository.debug(String.format("Caso de Uso: findVideoByIdUseCase -> Encontrado o video: %s", video));
        return video;
    }
}
