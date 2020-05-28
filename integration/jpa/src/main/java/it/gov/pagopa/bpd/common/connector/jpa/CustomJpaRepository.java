/*
 *
 */
package it.gov.pagopa.bpd.common.connector.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * The Class JPAConnectorImpl.
 *
 * @param <T>  the generic type
 * @param <ID> the generic type
 */
@NoRepositoryBean
public class CustomJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {


    public CustomJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public CustomJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    protected <S extends T> TypedQuery<S> getQuery(Specification<S> spec, Class<S> domainClass, Sort sort) {
        final IsEnabled<S> isEnable = new IsEnabled<>();
        return super.getQuery(isEnable.and(spec), domainClass, sort);
    }

    @Override
    protected <S extends T> TypedQuery<Long> getCountQuery(Specification<S> spec, Class<S> domainClass) {
        final IsEnabled<S> isEnable = new IsEnabled<>();
        return super.getCountQuery(isEnable.and(spec), domainClass);
    }

    protected static class IsEnabled<T> implements Specification<T> {
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.equal(root.get("enabled"), true);
        }
    }
}
