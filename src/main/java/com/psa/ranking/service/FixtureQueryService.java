package com.psa.ranking.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.psa.ranking.domain.Fixture;
import com.psa.ranking.domain.*; // for static metamodels
import com.psa.ranking.repository.FixtureRepository;
import com.psa.ranking.service.dto.FixtureCriteria;
import com.psa.ranking.service.dto.FixtureDTO;
import com.psa.ranking.service.mapper.FixtureMapper;

/**
 * Service for executing complex queries for {@link Fixture} entities in the database.
 * The main input is a {@link FixtureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FixtureDTO} or a {@link Page} of {@link FixtureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixtureQueryService extends QueryService<Fixture> {

    private final Logger log = LoggerFactory.getLogger(FixtureQueryService.class);

    private final FixtureRepository fixtureRepository;

    private final FixtureMapper fixtureMapper;

    public FixtureQueryService(FixtureRepository fixtureRepository, FixtureMapper fixtureMapper) {
        this.fixtureRepository = fixtureRepository;
        this.fixtureMapper = fixtureMapper;
    }

    /**
     * Return a {@link List} of {@link FixtureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixtureDTO> findByCriteria(FixtureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fixture> specification = createSpecification(criteria);
        return fixtureMapper.toDto(fixtureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FixtureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixtureDTO> findByCriteria(FixtureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fixture> specification = createSpecification(criteria);
        return fixtureRepository.findAll(specification, page)
            .map(fixtureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixtureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fixture> specification = createSpecification(criteria);
        return fixtureRepository.count(specification);
    }

    /**
     * Function to convert {@link FixtureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fixture> createSpecification(FixtureCriteria criteria) {
        Specification<Fixture> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Fixture_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Fixture_.status));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventId(),
                    root -> root.join(Fixture_.event, JoinType.LEFT).get(Event_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Fixture_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
