package com.nikita.shop.database.repository;

import com.nikita.shop.database.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Data
@Transactional
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {
    private final EntityManager entityManager;
    private final Class<E> entityClass;

    @Override
    @Transactional
    public E save(E entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(E entity) {
        entityManager.remove(
                entityManager.contains(entity) ? entity : entityManager.merge(entity)
        );
    }

    @Override
    @Transactional
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder()
                .createQuery(entityClass);
        criteria.select(criteria.from(entityClass));
        return entityManager.createQuery(criteria)
                .getResultList();
    }

}
