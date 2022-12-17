package br.com.aluraflix.infrastructure.repository.postgres.repository;

import br.com.aluraflix.domain.entity.Video;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostgresDBVideoRepositoryTests {

    @Autowired
    private PostgresDBVideoRepository postgresDBVideoRepository;
    private static EasyRandom easyRandom;

    @BeforeAll
    private static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @Test
    public void saveTest() throws Exception {
        Video video = easyRandom.nextObject(Video.class);
        video.setId(null);

        postgresDBVideoRepository.save(video);
        Optional<Video> optionalVideo = postgresDBVideoRepository.findById(video.getId());

        assertThat(optionalVideo).isPresent();

    }

    @Test
    public void findByIdTest() throws Exception {
        Video video = easyRandom.nextObject(Video.class);
        video.setId(null);
        postgresDBVideoRepository.save(video);

        Optional<Video> optionalVideoPresent = postgresDBVideoRepository.findById(video.getId());
        assertThat(optionalVideoPresent).isPresent();

        Optional<Video> optionalVideoNotPresent = postgresDBVideoRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThat(optionalVideoNotPresent).isEmpty();

    }

    @Test
    public void findAllTest() throws Exception {
        Video video = easyRandom.nextObject(Video.class);
        video.setId(null);
        postgresDBVideoRepository.save(video);

        List<Video> all = postgresDBVideoRepository.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(1);

    }

    @Test
    public void deleteByIdTest() throws Exception {
        Video video = easyRandom.nextObject(Video.class);
        video.setId(null);
        postgresDBVideoRepository.save(video);

        postgresDBVideoRepository.deleteById(video.getId());

        Optional<Video> optionalVideoNotPresent = postgresDBVideoRepository.findById(video.getId());
        assertThat(optionalVideoNotPresent).isEmpty();

    }
}
