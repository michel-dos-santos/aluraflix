package br.com.aluraflix.application.output;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class VideoResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private String url;

}
