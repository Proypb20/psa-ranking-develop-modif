package com.ar.pbpoints.repository;

import com.ar.pbpoints.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
	public Authority findByName(String name);
}
