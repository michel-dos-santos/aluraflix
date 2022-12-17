package br.com.aluraflix.application.rest;

import br.com.aluraflix.application.input.VideoRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateDescriptionRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateTitleRequestDTO;
import br.com.aluraflix.application.input.VideoUpdateURLRequestDTO;
import br.com.aluraflix.application.output.VideoResponseDTO;
import br.com.aluraflix.application.rest.exception.ExceptionResponse;
import br.com.aluraflix.domain.entity.Video;
import br.com.aluraflix.domain.exception.VideoNotFoundException;
import br.com.aluraflix.domain.port.repository.LogRepository;
import br.com.aluraflix.domain.port.usecase.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.exceptions.base.MockitoException;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VideoController.class)
public class VideoControllerTests extends ControllerTestBase {

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;
    @MockBean
    private FindAllVideosUseCase findAllVideosUseCase;
    @MockBean
    private FindVideoByIdUseCase findVideoByIdUseCase;
    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;
    @MockBean
    private DeleteVideoByIdUseCase deleteVideoByIdUseCase;
    @MockBean
    private LogRepository logRepository;

    private static String BASE_URL = VideoController.VIDEO_URL;

    @Test
    public void saveTest() throws Exception {
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);
        when(saveVideoUseCase.save(video)).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doPost(BASE_URL, videoRequestDTO, HttpStatus.OK, new TypeReference<VideoResponseDTO>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void saveEmptyFieldsTest() throws Exception {
        VideoRequestDTO videoRequestDTO = new VideoRequestDTO();

        ExceptionResponse exceptionResponse = doPost(BASE_URL, videoRequestDTO, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erros de validação direto do DTO");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(3);
        assertThat(exceptionResponse.getErrors().contains("title : O titulo do video deve ser informada")).isTrue();
        assertThat(exceptionResponse.getErrors().contains("description : A descrição do video deve ser informada")).isTrue();
        assertThat(exceptionResponse.getErrors().contains("url : A URL do video deve ser informada")).isTrue();

    }

    @Test
    public void saveInternalErrorExceptionTest() throws Exception {
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);
        when(saveVideoUseCase.save(video)).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doPost(BASE_URL, videoRequestDTO, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<ExceptionResponse>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao salvar a video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void findAllTest() throws Exception {
        List<VideoResponseDTO> videoResponseDTOList = easyRandom.objects(VideoResponseDTO.class, 10).collect(Collectors.toList());
        List<Video> videoList = easyRandom.objects(Video.class, 10).collect(Collectors.toList());
        Type type = new TypeToken<List<VideoResponseDTO>>() {}.getType();

        when(findAllVideosUseCase.findAll()).thenReturn(videoList);
        when(modelMapper.map(videoList, type)).thenReturn(videoResponseDTOList);

        List<VideoResponseDTO> videoResponseDTOListReturned = doGet(BASE_URL, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOListReturned).isEqualTo(videoResponseDTOList);

    }

    @Test
    public void findAllInternalErrorExceptionTest() throws Exception {
        when(findAllVideosUseCase.findAll()).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doGet(BASE_URL, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao buscar os videos");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void findByIdTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(findVideoByIdUseCase.findById(any())).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doGet(BASE_URL_WITH_ID, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void findByIdVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        when(findVideoByIdUseCase.findById(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        ExceptionResponse exceptionResponse = doGet(BASE_URL_WITH_ID, HttpStatus.NOT_FOUND, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void findByIdIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        ExceptionResponse exceptionResponse = doGet(BASE_URL_WITH_ID, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void findByIdInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        when(findVideoByIdUseCase.findById(any())).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doGet(BASE_URL_WITH_ID, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao buscar o video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void updateTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);
        when(updateVideoUseCase.update(any())).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doPut(BASE_URL_WITH_ID, videoRequestDTO, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void updateVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);
        when(updateVideoUseCase.update(any())).thenThrow(new VideoNotFoundException("id", id.toString()));

        ExceptionResponse exceptionResponse = doPut(BASE_URL_WITH_ID, videoRequestDTO, HttpStatus.NOT_FOUND, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void updateIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);

        ExceptionResponse exceptionResponse = doPut(BASE_URL_WITH_ID, videoRequestDTO, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void updateInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        VideoRequestDTO videoRequestDTO = easyRandom.nextObject(VideoRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);

        when(modelMapper.map(videoRequestDTO, Video.class)).thenReturn(video);
        when(updateVideoUseCase.update(any())).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doPut(BASE_URL_WITH_ID, videoRequestDTO, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao atualizar o video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void updateTitleTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/title", BASE_URL, id);
        VideoUpdateTitleRequestDTO videoUpdateTitleRequestDTO = easyRandom.nextObject(VideoUpdateTitleRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(updateVideoUseCase.updateTitle(id, videoUpdateTitleRequestDTO.getTitle())).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doPatch(BASE_URL_WITH_ID, videoUpdateTitleRequestDTO, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void updateTitleVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/title", BASE_URL, id);
        VideoUpdateTitleRequestDTO videoUpdateTitleRequestDTO = easyRandom.nextObject(VideoUpdateTitleRequestDTO.class);

        when(updateVideoUseCase.updateTitle(id, videoUpdateTitleRequestDTO.getTitle())).thenThrow(new VideoNotFoundException("id", id.toString()));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateTitleRequestDTO, HttpStatus.NOT_FOUND, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void updateTitleIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s/title", BASE_URL, id);
        VideoUpdateTitleRequestDTO videoUpdateTitleRequestDTO = easyRandom.nextObject(VideoUpdateTitleRequestDTO.class);

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateTitleRequestDTO, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void updateTitleInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/title", BASE_URL, id);
        VideoUpdateTitleRequestDTO videoUpdateTitleRequestDTO = easyRandom.nextObject(VideoUpdateTitleRequestDTO.class);

        when(updateVideoUseCase.updateTitle(id, videoUpdateTitleRequestDTO.getTitle())).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateTitleRequestDTO, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao atualizar o titulo do video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void updateDescriptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/description", BASE_URL, id);
        VideoUpdateDescriptionRequestDTO videoUpdateDescriptionRequestDTO = easyRandom.nextObject(VideoUpdateDescriptionRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(updateVideoUseCase.updateDescription(id, videoUpdateDescriptionRequestDTO.getDescription())).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doPatch(BASE_URL_WITH_ID, videoUpdateDescriptionRequestDTO, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void updateDescriptionVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/description", BASE_URL, id);
        VideoUpdateDescriptionRequestDTO videoUpdateDescriptionRequestDTO = easyRandom.nextObject(VideoUpdateDescriptionRequestDTO.class);

        when(updateVideoUseCase.updateDescription(id, videoUpdateDescriptionRequestDTO.getDescription())).thenThrow(new VideoNotFoundException("id", id.toString()));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateDescriptionRequestDTO, HttpStatus.NOT_FOUND, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void updateDescriptionIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s/description", BASE_URL, id);
        VideoUpdateDescriptionRequestDTO videoUpdateDescriptionRequestDTO = easyRandom.nextObject(VideoUpdateDescriptionRequestDTO.class);

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateDescriptionRequestDTO, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void updateDescriptionInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/description", BASE_URL, id);
        VideoUpdateDescriptionRequestDTO videoUpdateDescriptionRequestDTO = easyRandom.nextObject(VideoUpdateDescriptionRequestDTO.class);

        when(updateVideoUseCase.updateDescription(id, videoUpdateDescriptionRequestDTO.getDescription())).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateDescriptionRequestDTO, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao atualizar a descrição do video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

    @Test
    public void updateUrlTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/url", BASE_URL, id);
        VideoUpdateURLRequestDTO videoUpdateURLRequestDTO = easyRandom.nextObject(VideoUpdateURLRequestDTO.class);
        Video video = easyRandom.nextObject(Video.class);
        VideoResponseDTO videoResponseDTO = easyRandom.nextObject(VideoResponseDTO.class);

        when(updateVideoUseCase.updateUrl(id, videoUpdateURLRequestDTO.getUrl())).thenReturn(video);
        when(modelMapper.map(video, VideoResponseDTO.class)).thenReturn(videoResponseDTO);

        VideoResponseDTO videoResponseDTOReturned = doPatch(BASE_URL_WITH_ID, videoUpdateURLRequestDTO, HttpStatus.OK, new TypeReference<>() {
        }, 1L);

        assertThat(videoResponseDTOReturned).isEqualTo(videoResponseDTO);

    }

    @Test
    public void updateUrlVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/url", BASE_URL, id);
        VideoUpdateURLRequestDTO videoUpdateURLRequestDTO = easyRandom.nextObject(VideoUpdateURLRequestDTO.class);

        when(updateVideoUseCase.updateUrl(id, videoUpdateURLRequestDTO.getUrl())).thenThrow(new VideoNotFoundException("id", id.toString()));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateURLRequestDTO, HttpStatus.NOT_FOUND, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void updateUrlIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s/url", BASE_URL, id);
        VideoUpdateURLRequestDTO videoUpdateURLRequestDTO = easyRandom.nextObject(VideoUpdateURLRequestDTO.class);

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateURLRequestDTO, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void updateUrlInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s/url", BASE_URL, id);
        VideoUpdateURLRequestDTO videoUpdateURLRequestDTO = easyRandom.nextObject(VideoUpdateURLRequestDTO.class);

        when(updateVideoUseCase.updateUrl(id, videoUpdateURLRequestDTO.getUrl())).thenThrow(new MockitoException("error access database"));

        ExceptionResponse exceptionResponse = doPatch(BASE_URL_WITH_ID, videoUpdateURLRequestDTO, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        }, 1L);

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao atualizar a url do video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }




    @Test
    public void deleteByIdTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);
        String messageResponse = "Video excluido com sucesso";

        doNothing().when(deleteVideoByIdUseCase).deleteById(any());

        String messageReturned = doDelete(BASE_URL_WITH_ID, HttpStatus.OK, new TypeReference<>() {
        });

        assertThat(messageReturned).isEqualTo(messageResponse);

    }

    @Test
    public void deleteByIdVideoNotFoundExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        doThrow(new VideoNotFoundException("id", id.toString())).when(deleteVideoByIdUseCase).deleteById(any());

        ExceptionResponse exceptionResponse = doDelete(BASE_URL_WITH_ID, HttpStatus.NOT_FOUND, new TypeReference<>() {
        });

        assertThat(exceptionResponse.getDetails()).isEqualTo("Video não encontrado");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Video não encontrado com base no id: %s", id));

    }

    @Test
    public void deleteByIdIllegalArgumentExceptionTest() throws Exception {
        String id = "abc";
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        ExceptionResponse exceptionResponse = doDelete(BASE_URL_WITH_ID, HttpStatus.BAD_REQUEST, new TypeReference<>() {
        });

        assertThat(exceptionResponse.getDetails()).isEqualTo("Parâmetro inválido");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo(String.format("Invalid UUID string: %s", id));

    }

    @Test
    public void deleteByIdInternalErrorExceptionTest() throws Exception {
        UUID id = UUID.randomUUID();
        String BASE_URL_WITH_ID = String.format("%s/%s", BASE_URL, id);

        doThrow(new MockitoException("error access database")).when(deleteVideoByIdUseCase).deleteById(any());

        ExceptionResponse exceptionResponse = doDelete(BASE_URL_WITH_ID, HttpStatus.INTERNAL_SERVER_ERROR, new TypeReference<>() {
        });

        assertThat(exceptionResponse.getDetails()).isEqualTo("Erro interno inesperado ao excluir o video");
        assertThat(exceptionResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exceptionResponse.getErrors()).asList().hasSize(1);
        assertThat(exceptionResponse.getErrors().get(0)).isEqualTo("error access database");
    }

}
