package br.com.aluraflix.application.rest;


import br.com.aluraflix.application.input.VideoRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateDescriptionRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateTitleRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateURLRequestDTO;
import br.com.aluraflix.application.output.VideoResponseDTO;
import br.com.aluraflix.application.rest.exception.APIException;
import br.com.aluraflix.application.rest.exception.ExceptionResponse;
import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.usecase.*;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "Endpoint Videos")
@RestController
@RequestMapping(path = VideoController.VIDEO_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VideoController {

    public static final String VIDEO_URL = "/videos";

    @Autowired
    private SaveVideoUseCase saveVideoUseCase;
    @Autowired
    private FindAllVideosUseCase findAllVideosUseCase;
    @Autowired
    private FindVideoByIdUseCase findVideoByIdUseCase;
    @Autowired
    private UpdateVideoUseCase updateVideoUseCase;
    @Autowired
    private DeleteVideoByIdUseCase deleteVideoByIdUseCase;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LogRepository logRepository;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que o video foi salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao salvar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Salva na base os dados do video")
    @Counted(value = "execution.count.videos.save")
    @Timed(value = "execution.time.videos.save", longTask = true)
    @PostMapping
    public VideoResponseDTO save(@RequestBody @Valid VideoRequestDTO videoRequestDTO) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos"));
            Video video = saveVideoUseCase.save(modelMapper.map(videoRequestDTO, Video.class));
            return modelMapper.map(video, VideoResponseDTO.class);
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao salvar a video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a busca foi executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao buscar os videos", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Busca na base todos os videos")
    @Counted(value = "execution.count.videos.findAll")
    @Timed(value = "execution.time.videos.findAll", longTask = true)
    @GetMapping
    public List<VideoResponseDTO> findAll() throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos"));
            List<Video> allVideos = findAllVideosUseCase.findAll();
            Type type = new TypeToken<List<VideoResponseDTO>>() {}.getType();

            return modelMapper.map(allVideos, type);
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao buscar os videos", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a busca foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao buscar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Busca na base os dados o video com o ID informado")
    @Counted(value = "execution.count.videos.findById")
    @Timed(value = "execution.time.videos.findById", longTask = true)
    @GetMapping(value = "/{id}")
    public VideoResponseDTO findById(@PathVariable(value = "id") String id) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}"));
            Video video = findVideoByIdUseCase.findById(UUID.fromString(id));
            return modelMapper.map(video, VideoResponseDTO.class);
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao buscar o video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a atualização foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado para ser atualizado"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao atualizar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Atualiza o video com base nos dados informado")
    @Counted(value = "execution.count.videos.update")
    @Timed(value = "execution.time.videos.update", longTask = true)
    @PutMapping(value = "/{id}")
    public VideoResponseDTO update(@PathVariable(value = "id") String id, @RequestBody @Valid VideoRequestDTO videoRequestDTO) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}"));
            Video video = modelMapper.map(videoRequestDTO, Video.class);
            video.setId(UUID.fromString(id));
            return modelMapper.map(updateVideoUseCase.update(video), VideoResponseDTO.class);
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao atualizar o video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a atualização foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado para ser atualizado"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao atualizar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Atualiza o titulo do video com base nos dados informado")
    @Counted(value = "execution.count.videos.updateTitle")
    @Timed(value = "execution.time.videos.updateTitle", longTask = true)
    @PatchMapping(value = "/{id}/title")
    public VideoResponseDTO updateTitle(@PathVariable(value = "id") String id, @RequestBody @Valid VideoUpdateTitleRequestDTO videoUpdateTitleRequestDTO) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}/title"));
            Video video = updateVideoUseCase.updateTitle(UUID.fromString(id), videoUpdateTitleRequestDTO.getTitle());
            return modelMapper.map(video, VideoResponseDTO.class);
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao atualizar o titulo do video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a atualização foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado para ser atualizado"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao atualizar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Atualiza a descrição do video com base nos dados informado")
    @Counted(value = "execution.count.videos.updateDescription")
    @Timed(value = "execution.time.videos.updateDescription", longTask = true)
    @PatchMapping(value = "/{id}/description")
    public VideoResponseDTO updateDescription(@PathVariable(value = "id") String id, @RequestBody @Valid VideoUpdateDescriptionRequestDTO videoUpdateDescriptionRequestDTO) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}/description"));
            Video video = updateVideoUseCase.updateDescription(UUID.fromString(id), videoUpdateDescriptionRequestDTO.getDescription());
            return modelMapper.map(video, VideoResponseDTO.class);
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao atualizar a descrição do video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a atualização foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado para ser atualizado"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao atualizar o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Atualiza a url do video com base nos dados informado")
    @Counted(value = "execution.count.videos.updateUrl")
    @Timed(value = "execution.time.videos.updateUrl", longTask = true)
    @PatchMapping(value = "/{id}/url")
    public VideoResponseDTO updateUrl(@PathVariable(value = "id") String id, @RequestBody @Valid VideoUpdateURLRequestDTO videoUpdateURLRequestDTO) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}/url"));
            Video video = updateVideoUseCase.updateUrl(UUID.fromString(id), videoUpdateURLRequestDTO.getUrl());
            return modelMapper.map(video, VideoResponseDTO.class);
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao atualizar a url do video", e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indica que a atualização foi executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Indica que houve um erro na requisição"),
            @ApiResponse(responseCode = "404", description = "Indica que a busca foi executada com sucesso, porém não foi encontrado nenhum resultado para ser excluido"),
            @ApiResponse(responseCode = "500", description = "Indica que houve algum erro inesperado ao excluir o video", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @Operation(summary = "Exclui o video com base no ID informado")
    @Counted(value = "execution.count.videos.deleteById")
    @Timed(value = "execution.time.videos.deleteById", longTask = true)
    @DeleteMapping(value = "/{id}")
    public String deleteById(@PathVariable(value = "id") String id) throws APIException {
        try {
            logRepository.debug(String.format("Executando: %s -> Recebendo requisição para %s", VideoController.class.getName(), "/videos/{id}"));
            deleteVideoByIdUseCase.deleteById(UUID.fromString(id));
            return "Video excluido com sucesso";
        } catch (VideoNotFoundException e) {
            throw APIException.notFound("Video não encontrado", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw APIException.badRequest("Parâmetro inválido", e.getMessage());
        } catch (Exception e) {
            throw APIException.internalError("Erro interno inesperado ao excluir o video", e.getMessage());
        }
    }
}

