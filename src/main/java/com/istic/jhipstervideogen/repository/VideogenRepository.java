package com.istic.jhipstervideogen.repository;

import com.istic.jhipstervideogen.domain.Videogen;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Videogen entity.
 */
public interface VideogenRepository extends JpaRepository<Videogen,Long> {

}
