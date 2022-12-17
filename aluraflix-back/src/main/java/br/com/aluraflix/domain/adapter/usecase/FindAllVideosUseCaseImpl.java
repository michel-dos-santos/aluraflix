package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.FindAllVideosUseCase;

import java.util.List;

public class FindAllVideosUseCaseImpl implements FindAllVideosUseCase {

    private final VideoRepository videoRepository;
    private final LogRepository logRepository;

    public FindAllVideosUseCaseImpl(VideoRepository videoRepository, LogRepository logRepository) {
        this.videoRepository = videoRepository;
        this.logRepository = logRepository;
    }

    @Override
    public List<Video> findAll() {
        logRepository.debug("Caso de Uso: findAllVideosUseCase -> Iniciando o processo de busca de todos os videos");
        List<Video> all = videoRepository.findAll();
        logRepository.debug(String.format("Caso de Uso: findAllVideosUseCase -> Encontrado %s videos", all.size()));
        return all;
    }
}
