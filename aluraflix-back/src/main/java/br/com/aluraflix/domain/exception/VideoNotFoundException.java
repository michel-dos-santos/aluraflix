package br.com.aluraflix.domain.exception;

public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(String campo, String conteudo) {
        super(String.format("Video não encontrado com base no %s: %s", campo, conteudo));
    }

}