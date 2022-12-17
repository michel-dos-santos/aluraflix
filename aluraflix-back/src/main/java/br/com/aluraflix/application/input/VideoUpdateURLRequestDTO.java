package br.com.aluraflix.application.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class VideoUpdateURLRequestDTO {

    @NotBlank(message = "A URL do video deve ser informada")
    private String url;

    @Override
    public String toString() {
        return String.format("url=%s", this.url);
    }

}
