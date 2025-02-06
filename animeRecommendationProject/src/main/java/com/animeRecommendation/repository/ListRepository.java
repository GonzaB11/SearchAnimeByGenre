package com.animeRecommendation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animeRecommendation.model.Anime;
import com.animeRecommendation.model.ListAnime;
import com.animeRecommendation.model.User;

public interface ListRepository extends JpaRepository<ListAnime, Long>{

	List<ListAnime> findByUser(User user);
    Optional<ListAnime> findByUserAndAnime(User user, Anime anime);
	
}
