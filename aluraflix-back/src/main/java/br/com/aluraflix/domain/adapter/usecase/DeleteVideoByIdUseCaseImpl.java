package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.DeleteVideoByIdUseCase;

import java.util.UUID;

public class DeleteVideoByIdUseCaseImpl implements DeleteVideoByIdUseCase {

    private final VideoRepository videoRepository;
    private final LogRepository logRepository;

    public DeleteVideoByIdUseCaseImpl(VideoRepository videoRepository, LogRepository logRepository) {
        this.videoRepository = videoRepository;
        this.logRepository = logRepository;
    }

    @Override
    public void deleteById(UUID id) {
        logRepository.debug(String.format("Caso de Uso: deleteVideoByIdUseCase -> Iniciando o processo de exclusão do video pelo id: %s", id));
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException("id", id.toString()));
        videoRepository.deleteById(id);
        logRepository.debug(String.format("Caso de Uso: deleteVideoByIdUseCase -> Excluido o video: %s", video));
    }
}
