package br.com.aluraflix.application.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class VideoUpdateTitleRequestDTO {

    @NotBlank(message = "O titulo do video deve ser informada")
    private String title;

    @Override
    public String toString() {
        return String.format("title=%s", this.title);
    }

}
