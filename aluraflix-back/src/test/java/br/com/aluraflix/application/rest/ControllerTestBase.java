package br.com.aluraflix.application.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    @Qualifier("mapperApi")
    protected ModelMapper modelMapper;

    protected static EasyRandom easyRandom;
    protected static ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeTests() {
        easyRandom = new EasyRandom();
        objectMapper = new ObjectMapper();
    }

    protected ResultActions doRequest(RequestBuilder requestBuilder, HttpStatus expectedStatus)
            throws Exception {
        return mockMvc
                .perform(requestBuilder)
                .andExpect(status().is(expectedStatus.value()));
    }

    protected <T> T doRequest(RequestBuilder requestBuilder, HttpStatus expectedStatus, TypeReference<T> responseType)
            throws Exception {
        byte[] bytes = doRequest(requestBuilder, expectedStatus)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected ResultActions doGet(String url, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(get(url, uriVars)
                                 .contentType(MediaType.APPLICATION_JSON),
                         expectedStatus);
    }

    protected <T> T doGet(String url, HttpStatus expectedStatus, TypeReference<T> responseType, Object... uriVars) throws Exception {
        byte[] bytes = doGet(url, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected ResultActions doPost(String url, Object body, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(
                post(url, uriVars)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body)),
                expectedStatus);
    }

    protected <T> T doPost(String url, Object body, HttpStatus expectedStatus, TypeReference<T> responseType,
            Object... uriVars) throws Exception {
        byte[] bytes = doPost(url, body, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected ResultActions doPut(String url, Object body, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(put(url, uriVars)
                                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 .content(objectMapper.writeValueAsString(body)),
                         expectedStatus);
    }

    protected <T> T doPut(String url, Object body, HttpStatus expectedStatus, TypeReference<T> responseType,
            Object... uriVars) throws Exception {
        byte[] bytes = doPut(url, body, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected ResultActions doPatch(String url, Object body, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(patch(url, uriVars)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body)),
                expectedStatus);
    }

    protected <T> T doPatch(String url, Object body, HttpStatus expectedStatus, TypeReference<T> responseType,
                          Object... uriVars) throws Exception {
        byte[] bytes = doPatch(url, body, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected ResultActions doDelete(String url, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(delete(url, uriVars),
                expectedStatus);
    }

    protected <T> T doDelete(String url, HttpStatus expectedStatus, TypeReference<T> responseType) throws Exception {
        byte[] bytes = doDelete(url, expectedStatus, 1L)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        try {
            return objectMapper.readValue(bytes, responseType);
        } catch (Exception e) {
            return (T) new String(bytes);
        }
    }
}
