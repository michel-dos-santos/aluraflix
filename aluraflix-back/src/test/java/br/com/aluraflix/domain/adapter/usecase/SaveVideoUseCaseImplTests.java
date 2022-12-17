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

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SaveVideoUseCaseImplTests {

    @InjectMocks
    private SaveVideoUseCaseImpl saveVideoUseCase;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private LogRepository logRepository;
    private static EasyRandom easyRandom;

    @BeforeAll
    private static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @Test
    public void saveTest() throws Exception {
        List<Video> videos = easyRandom.objects(Video.class, 10).collect(Collectors.toList());

        videos.forEach(video -> {
            saveVideoUseCase.save(video);
            verify(videoRepository).save(video);
        });

        verify(videoRepository, atLeast(10)).save(any());
        verify(logRepository, atLeast(20)).debug(any());

    }

}
