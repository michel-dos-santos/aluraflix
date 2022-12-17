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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateVideoUseCaseImplTests {

    @InjectMocks
    private UpdateVideoUseCaseImpl updateVideoUseCase;
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
    public void update() {
        Video video = easyRandom.nextObject(Video.class);
        when(videoRepository.findById(any())).thenReturn(Optional.of(video));
        when(videoRepository.save(any())).thenReturn(video);

        Video videoUpdated = updateVideoUseCase.update(video);

        assertThat(videoUpdated).isEqualTo(video);
        verify(videoRepository, atLeast(1)).findById(any());
        verify(videoRepository, atLeast(1)).save(any());
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void updateException() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        when(videoRepository.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        assertThatThrownBy(() -> updateVideoUseCase.update(video))
                .isInstanceOf(VideoNotFoundException.class)
                .hasMessage(String.format("Video não encontrado com base no %s: %s", "id", id));

        verify(videoRepository, atLeast(1)).findById(any());
        verify(logRepository, atLeast(1)).debug(any());

    }

    @Test
    public void updateTitle() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String title = easyRandom.nextObject(String.class);
        video.setTitle(title);
        when(videoRepository.findById(any())).thenReturn(Optional.of(video));
        when(videoRepository.save(any())).thenReturn(video);

        Video videoUpdated = updateVideoUseCase.updateTitle(id, title);

        assertThat(videoUpdated).isEqualTo(video);
        verify(videoRepository, atLeast(1)).findById(any());
        verify(videoRepository, atLeast(1)).save(any());
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void updateTitleException() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String title = easyRandom.nextObject(String.class);
        video.setTitle(title);
        when(videoRepository.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        assertThatThrownBy(() -> updateVideoUseCase.updateTitle(id, title))
                .isInstanceOf(VideoNotFoundException.class)
                .hasMessage(String.format("Video não encontrado com base no %s: %s", "id", id));

        verify(videoRepository, atLeast(1)).findById(any());
        verify(logRepository, atLeast(1)).debug(any());

    }

    @Test
    public void updateDescription() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String description = easyRandom.nextObject(String.class);
        video.setDescription(description);
        when(videoRepository.findById(any())).thenReturn(Optional.of(video));
        when(videoRepository.save(any())).thenReturn(video);

        Video videoUpdated = updateVideoUseCase.updateDescription(id, description);

        assertThat(videoUpdated).isEqualTo(video);
        verify(videoRepository, atLeast(1)).findById(any());
        verify(videoRepository, atLeast(1)).save(any());
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void updateDescriptionException() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String description = easyRandom.nextObject(String.class);
        video.setDescription(description);
        when(videoRepository.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        assertThatThrownBy(() -> updateVideoUseCase.updateDescription(id, description))
                .isInstanceOf(VideoNotFoundException.class)
                .hasMessage(String.format("Video não encontrado com base no %s: %s", "id", id));

        verify(videoRepository, atLeast(1)).findById(any());
        verify(logRepository, atLeast(1)).debug(any());

    }

    @Test
    public void updateUrl() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String url = easyRandom.nextObject(String.class);
        video.setUrl(url);
        when(videoRepository.findById(any())).thenReturn(Optional.of(video));
        when(videoRepository.save(any())).thenReturn(video);

        Video videoUpdated = updateVideoUseCase.updateUrl(id, url);

        assertThat(videoUpdated).isEqualTo(video);
        verify(videoRepository, atLeast(1)).findById(any());
        verify(videoRepository, atLeast(1)).save(any());
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void updateUrlException() {
        Video video = easyRandom.nextObject(Video.class);
        UUID id = UUID.randomUUID();
        String url = easyRandom.nextObject(String.class);
        video.setUrl(url);
        when(videoRepository.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        assertThatThrownBy(() -> updateVideoUseCase.updateUrl(id, url))
                .isInstanceOf(VideoNotFoundException.class)
                .hasMessage(String.format("Video não encontrado com base no %s: %s", "id", id));

        verify(videoRepository, atLeast(1)).findById(any());
        verify(logRepository, atLeast(1)).debug(any());

    }
}
