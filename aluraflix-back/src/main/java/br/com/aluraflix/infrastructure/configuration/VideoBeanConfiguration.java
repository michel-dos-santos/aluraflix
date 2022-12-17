package br.com.aluraflix.infrastructure.configuration;

import br.com.aluraflix.domain.adapter.usecase.*;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.domain.port.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoBeanConfiguration {

    @Bean
    SaveVideoUseCase saveVideoUseCase(VideoRepository videoRepository, LogRepository logRepository) {
        return new SaveVideoUseCaseImpl(videoRepository, logRepository);
    }

    @Bean
    FindAllVideosUseCase findAllVideosUseCase(VideoRepository videoRepository, LogRepository logRepository) {
        return new FindAllVideosUseCaseImpl(videoRepository, logRepository);
    }

    @Bean
    FindVideoByIdUseCase findVideoByIdUseCase(VideoRepository videoRepository, LogRepository logRepository) {
        return new FindVideoByIdUseCaseImpl(videoRepository, logRepository);
    }

    @Bean
    UpdateVideoUseCase updateVideoUseCase(VideoRepository videoRepository, LogRepository logRepository) {
        return new UpdateVideoUseCaseImpl(videoRepository, logRepository);
    }

    @Bean
    DeleteVideoByIdUseCase deleteVideoByIdUseCase(VideoRepository videoRepository, LogRepository logRepository) {
        return new DeleteVideoByIdUseCaseImpl(videoRepository, logRepository);
    }

}
