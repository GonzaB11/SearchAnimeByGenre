package com.animeRecommendation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animeRecommendation.model.Anime;
import com.animeRecommendation.service.AnimeService;

@RestController
@RequestMapping("/animes")
public class AnimeController {

	private AnimeService animeService;
	
	@Autowired
	public AnimeController(AnimeService animeService) {
		this.animeService = animeService;
	}
	
	@GetMapping
	public ResponseEntity<List<Anime>> getAnimes(){
		List<Anime> animes = animeService.getAnimes();
		if(animes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(animes, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Anime> getAnimeById(@PathVariable Long id){
		try {
			Anime anime = animeService.getAnimeById(id);
			return new ResponseEntity<>(anime, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<String> addAnime(@RequestBody Anime anime){
		try {
			animeService.addAnime(anime);
			return new ResponseEntity<>("Anime added successfully.", HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAnime(@PathVariable Long id){
		try {
			animeService.deleteAnime(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Anime> updateAnime(@PathVariable Long id, @RequestBody Anime anime){
		try {
			Anime updateTheAnime = animeService.updateAnime(id, anime);
			return new ResponseEntity<>(updateTheAnime, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}	
	
	@GetMapping(params = "name")
	public ResponseEntity<List<Anime>> getAnimeByName(@RequestParam String name) {
	    List<Anime> animes = animeService.getAnimeByName(name);
	    return animes.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(animes, HttpStatus.OK);
	}
	
	@GetMapping("/search")
    public ResponseEntity<List<Anime>> searchAnimesByGenres(@RequestParam String genres) {
        List<Anime> animes = animeService.searchAnimesByGenres(genres);
        return ResponseEntity.ok(animes);
    }
}