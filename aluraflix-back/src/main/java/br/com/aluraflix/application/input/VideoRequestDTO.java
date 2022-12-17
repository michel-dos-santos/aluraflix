package br.com.aluraflix.application.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class VideoRequestDTO {

    @NotBlank(message = "O titulo do video deve ser informada")
    protected String title;

    @NotBlank(message = "A descrição do video deve ser informada")
    protected String description;

    @NotBlank(message = "A URL do video deve ser informada")
    protected String url;

    @Override
    public String toString() {
        return String.format("title=%s, description=%s, url=%s", this.title, this.description, this.url);
    }

}
