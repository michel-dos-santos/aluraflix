package br.com.aluraflix.domain.port.usecase;

import br.com.aluraflix.domain.entity.Video;

import java.util.List;

public interface FindAllVideosUseCase {

    List<Video> findAll();

}
