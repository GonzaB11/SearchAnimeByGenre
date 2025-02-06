package com.animeRecommendation.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ListAnime> animeList = new ArrayList<>();
	
	public User() {	
	}
	
	public User(String name) {
		this.name= name;	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ListAnime> getAnimeList() {
		return animeList;
	}

	public void setAnimeList(List<ListAnime> animeList) {
		this.animeList = animeList;
	}

	@Override
	public String toString() {
		return "User{" + 
				"id=" + id + 
				", name=" + name + 
				", animeList=" + animeList + 
				'}';
	}	
}
