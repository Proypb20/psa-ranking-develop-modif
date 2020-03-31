package com.ar.pbpoints.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.pbpoints.domain.User;
import com.ar.pbpoints.domain.UserExtra;


/**
 * Spring Data  repository for the UserExtra entity.
 */
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long> {

	public Optional<UserExtra> findByUser (User user);

}
