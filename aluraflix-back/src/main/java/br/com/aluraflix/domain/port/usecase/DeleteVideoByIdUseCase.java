package br.com.aluraflix.domain.port.usecase;

import java.util.UUID;

public interface DeleteVideoByIdUseCase {

    void deleteById(UUID id);

}
