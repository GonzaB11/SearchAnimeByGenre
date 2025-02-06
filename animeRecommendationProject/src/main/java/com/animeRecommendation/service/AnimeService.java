package com.animeRecommendation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animeRecommendation.model.Anime;
import com.animeRecommendation.repository.AnimeRepository;

import jakarta.transaction.Transactional;

@Service
public class AnimeService {

	@Autowired
	private AnimeRepository animeRepository;
	
	public List<Anime> getAnimes(){
		return animeRepository.findAll();
	}
	
	public int getSizeAnime() {
		return (int) animeRepository.count();
	}

	public Anime getAnimeById(Long id) {
		return animeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("The anime doesn't exist"));
	}
	
	public void addAnime(Anime anime) {
	    boolean exists = animeRepository.existsByName(anime.getName());
	    if (exists) {
	        throw new RuntimeException("The anime already exists with the same name");
	    }
	    animeRepository.save(anime);
	}
	
	@Transactional
	public void deleteAnime(Long id) {
		if(!animeRepository.existsById(id)) {
			throw new RuntimeException("The anime doesn't exist");
		}
		animeRepository.deleteById(id);
	}
	
	@Transactional
	public Anime updateAnime(Long id, Anime anime) {
		Anime existingAnime = getAnimeById(id);
        existingAnime.setName(anime.getName());
        existingAnime.setGenres(anime.getGenres());
        existingAnime.setYear(anime.getYear());
        existingAnime.setRanking(anime.getRanking());
        existingAnime.setUrl(anime.getUrl());
        existingAnime.setDescription(anime.getDescription());

        return animeRepository.save(existingAnime);
	}
	
	public List<Anime> getAnimeByName(String name) {
	    return animeRepository.findByNameContainingIgnoreCase(name);
	}
	
	public List<Anime> searchAnimesByGenres(String genres) {
        return animeRepository.findByGenresContaining(genres);
    }
}