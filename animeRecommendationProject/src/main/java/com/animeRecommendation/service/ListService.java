package com.animeRecommendation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animeRecommendation.model.Anime;
import com.animeRecommendation.model.ListAnime;
import com.animeRecommendation.model.User;
import com.animeRecommendation.repository.AnimeRepository;
import com.animeRecommendation.repository.ListRepository;
import com.animeRecommendation.repository.UserRepository;

@Service
public class ListService {
	
	@Autowired 
	private ListRepository animeListRepository; 

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnimeRepository animeRepository;

	public List<ListAnime> getListByUser(Long userId){
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("The user with ID " + userId + " doesn't exist"));
		return animeListRepository.findByUser(user);
	}
	
	public ListAnime addAnimeToList(Long userId, Long animeId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("The user with ID " + userId + " doesn't exist"));
	
		Anime anime = animeRepository.findById(animeId)
				.orElseThrow(() -> new RuntimeException("The anime with ID " + animeId + " doesn't exist"));
	
		if(animeListRepository.findByUserAndAnime(user, anime).isPresent()) {
			 throw new RuntimeException("The anime is already in the user's list");
		}
		
		ListAnime listAnime = new ListAnime();
		listAnime.setUser(user);
		listAnime.setAnime(anime);
		
		return animeListRepository.save(listAnime);
	}
	
	public void removeAnimeFromList(Long userId, Long animeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("The user with ID " + userId + " doesn't exist"));

        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new RuntimeException("The anime with ID " + animeId + " doesn't exist"));

        
        ListAnime listAnime = animeListRepository.findByUserAndAnime(user, anime)
                .orElseThrow(() -> new RuntimeException("The anime is not in the user's list"));

        animeListRepository.delete(listAnime);
    }
}
