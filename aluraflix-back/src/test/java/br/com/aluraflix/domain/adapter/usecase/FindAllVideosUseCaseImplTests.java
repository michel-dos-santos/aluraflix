package br.com.aluraflix.domain.adapter.usecase;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAllVideosUseCaseImplTests {

    @InjectMocks
    private FindAllVideosUseCaseImpl findAllVideosUseCase;
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
    public void findAll() {
        List<Video> videos = easyRandom.objects(Video.class, 10).collect(Collectors.toList());
        when(videoRepository.findAll()).thenReturn(videos);

        List<Video> all = findAllVideosUseCase.findAll();

        assertThat(all).hasSize(10);
        assertThat(all).containsAll(videos);
        verify(videoRepository, atLeast(1)).findAll();
        verify(logRepository, atLeast(2)).debug(any());

    }

    @Test
    public void findAllReturnZero() {
        List<Video> videos = Collections.EMPTY_LIST;
        when(videoRepository.findAll()).thenReturn(videos);

        List<Video> all = findAllVideosUseCase.findAll();

        assertThat(all).hasSize(0);
        assertThat(all).containsAll(videos);
        verify(videoRepository, atLeast(1)).findAll();
        verify(logRepository, atLeast(2)).debug(any());

    }

}
