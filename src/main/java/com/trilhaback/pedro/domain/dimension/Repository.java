package com.trilhaback.pedro.domain.dimension;

import java.util.List;

public interface Repository<T> {

    T insert(T t);
    T update(T t);
    T findById(Long id);
    int deleteById(Long id);
    T findByName(String name);
    List<T> findAll();

}
