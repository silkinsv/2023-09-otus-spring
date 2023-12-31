package ru.otus.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Set<Genre> findAllByIds(List<Long> ids) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id in :ids ", Genre.class);
        query.setParameter("ids", ids);
        return new HashSet<>(query.getResultList());
    }
}
