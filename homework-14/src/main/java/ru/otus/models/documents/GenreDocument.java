package ru.otus.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "genres")
public class GenreDocument {
    @Id
    private String id;

    private String name;

    private Long migrationId;

    public GenreDocument(String name, Long migrationId) {
        this.name = name;
        this.migrationId = migrationId;
    }
}
