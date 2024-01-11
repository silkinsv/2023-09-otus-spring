package ru.otus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    @NotEmpty(message = "Title should not empty")
    private String title;

    @NotNull
    private String authorId;

    @NotNull
    private String genreId;
}
