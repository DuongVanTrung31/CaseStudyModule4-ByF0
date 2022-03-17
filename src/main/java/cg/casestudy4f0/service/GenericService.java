package cg.casestudy4f0.service;

import java.util.Optional;

public interface GenericService<T> {
    Iterable<T> findAll();

    Optional<T> findById(Long id);

    T save(T t);

    void remove(Long id);
}
