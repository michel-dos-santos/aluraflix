package br.com.aluraflix.infrastructure.repository.postgres.repository;

import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.port.repository.VideoRepository;
import br.com.aluraflix.infrastructure.repository.postgres.entity.VideoEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresDBVideoRepository implements VideoRepository {

    private final SpringDataPostgresVideoRepository videoRepository;
    private final ModelMapper modelMapper;

    public PostgresDBVideoRepository(SpringDataPostgresVideoRepository videoRepository, ModelMapper modelMapper) {
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Video save(Video video) {
        VideoEntity videoEntity = modelMapper.map(video, VideoEntity.class);
        videoRepository.save(videoEntity);
        video.setId(videoEntity.getId());
        return video;
    }

    @Override
    public List<Video> findAll() {
        List<VideoEntity> entityList = videoRepository.findAll();

        Type type = new TypeToken<List<Video>>() {}.getType();
        return modelMapper.map(entityList, type);
    }

    @Override
    public Optional<Video> findById(UUID id) {
        Optional<VideoEntity> optionalVideoEntity = videoRepository.findById(id);

        if (optionalVideoEntity.isPresent()) {
            return Optional.of(modelMapper.map(optionalVideoEntity.get(), Video.class));
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(UUID id) {
        videoRepository.deleteById(id);
    }
}
