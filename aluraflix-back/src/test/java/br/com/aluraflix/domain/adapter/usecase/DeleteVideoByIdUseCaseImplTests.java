package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteVideoByIdUseCaseImplTests {

    @InjectMocks
    private DeleteVideoByIdUseCaseImpl deleteVideoByIdUseCase;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private LogRepository logRepository;
    private static EasyRandom easyRandom;

    @BeforeAll
    public static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @Test
    public void findById() {
        Video video = easyRandom.nextObject(Video.class);
        when(videoRepository.findById(any())).thenReturn(Optional.of(video));
        doNothing().when(videoRepository).deleteById(any());

        deleteVideoByIdUseCase.deleteById(UUID.randomUUID());

        verify(videoRepository, atLeast(1)).findById(any());
        verify(videoRepository, atLeast(1)).deleteById(any());
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void findByIdException() {
        UUID id = UUID.randomUUID();
        when(videoRepository.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        assertThatThrownBy(() -> deleteVideoByIdUseCase.deleteById(any()))
                .isInstanceOf(VideoNotFoundException.class)
                .hasMessage(String.format("Video não encontrado com base no %s: %s", "id", id));

        verify(videoRepository, atLeast(1)).findById(any());
        verify(logRepository, atLeast(1)).debug(any());
    }

}
