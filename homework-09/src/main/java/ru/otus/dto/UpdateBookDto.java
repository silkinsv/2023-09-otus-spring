package ru.otus.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String title;

    @NotNull
    private Long authorId;

    @NotNull
    private Set<Long> genreIds;
}
