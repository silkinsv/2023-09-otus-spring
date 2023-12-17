package ru.otus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genres;
}
