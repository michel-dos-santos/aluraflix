package br.com.aluraflix.application.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class VideoUpdateDescriptionRequestDTO {

    @NotBlank(message = "A descrição do video deve ser informada")
    private String description;

    @Override
    public String toString() {
        return String.format("description=%s", this.description);
    }

}
