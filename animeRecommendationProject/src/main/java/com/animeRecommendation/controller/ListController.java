package com.animeRecommendation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animeRecommendation.model.ListAnime;
import com.animeRecommendation.service.ListService;

@RestController
@RequestMapping("/userListAnimes")
public class ListController {
	
	private ListService listService;
	
	@Autowired
	public ListController(ListService listService) {
		this.listService = listService;
	}
	
	@GetMapping("/{userId}")
    public ResponseEntity<List<ListAnime>> getUserAnimeList(@PathVariable Long userId) {
        List<ListAnime> animeList = listService.getListByUser(userId);
        return ResponseEntity.ok(animeList);
    }
	
	@PostMapping
    public ResponseEntity<ListAnime> addAnimeToList(
            @RequestParam Long userId,
            @RequestParam Long animeId) {
        ListAnime savedAnime = listService.addAnimeToList(userId, animeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnime);
    }
	
	@DeleteMapping
    public ResponseEntity<Void> removeAnimeFromList(
            @RequestParam Long userId,
            @RequestParam Long animeId) {
        listService.removeAnimeFromList(userId, animeId);
        return ResponseEntity.noContent().build();
    }
}
