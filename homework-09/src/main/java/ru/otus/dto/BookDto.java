package ru.otus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private AuthorDto author;

    private Set<GenreDto> genres;
}
