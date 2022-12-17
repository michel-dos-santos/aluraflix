package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.UpdateVideoUseCase;

import java.util.UUID;

public class UpdateVideoUseCaseImpl implements UpdateVideoUseCase {

    private final VideoRepository videoRepository;
    private final LogRepository logRepository;

    public UpdateVideoUseCaseImpl(VideoRepository videoRepository, LogRepository logRepository) {
        this.videoRepository = videoRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Video update(Video video) {
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Iniciando o processo de atualização dos dados do video: %s", video));
        videoRepository.findById(video.getId()).orElseThrow(() -> new VideoNotFoundException("id", video.getId().toString()));
        videoRepository.save(video);
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Finalizando o processo de atualização dos dados do video: %s", video));
        return video;
    }

    @Override
    public Video updateTitle(UUID id, String title) {
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Iniciando o processo de atualização do titulo (%s) do video com base no id: %s", title, id));
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException("id", id.toString()));
        video.setTitle(title);
        videoRepository.save(video);
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Finalizando o processo de atualização do titulo (%s) do video com base no id: %s", title, id));
        return video;
    }

    @Override
    public Video updateDescription(UUID id, String description) {
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Iniciando o processo de atualização da descrição (%s) do video com base no id: %s", description, id));
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException("id", id.toString()));
        video.setDescription(description);
        videoRepository.save(video);
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Finalizando o processo de atualização da descrição (%s) do video com base no id: %s", description, id));
        return video;
    }

    @Override
    public Video updateUrl(UUID id, String url) {
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Iniciando o processo de atualização da url (%s) do video com base no id: %s", url, id));
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException("id", id.toString()));
        video.setUrl(url);
        videoRepository.save(video);
        logRepository.debug(String.format("Caso de Uso: updateVideoUseCase -> Finalizando o processo de atualização da url (%s) do video com base no id: %s", url, id));
        return video;
    }
}
