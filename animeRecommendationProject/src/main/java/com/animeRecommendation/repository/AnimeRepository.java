package com.animeRecommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.animeRecommendation.model.Anime;

public interface AnimeRepository extends JpaRepository<Anime , Long>{
	@Query("SELECT a FROM Anime a WHERE a.genres LIKE %:genres%")
	List<Anime> findByGenresContaining(@Param("genres") String genres);
	boolean existsByName(String name);

	List<Anime> findByNameContainingIgnoreCase(String name);
}