package com.psa.ranking.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psa.ranking.domain.User;
import com.psa.ranking.domain.UserExtra;


/**
 * Spring Data  repository for the UserExtra entity.
 */
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long> {

	public Optional<UserExtra> findByUser (User user);
	
}
