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
public class UpdateBookDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private Long authorId;

    private Set<Long> genreIds;
}
