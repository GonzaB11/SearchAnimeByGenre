package com.animeRecommendation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "anime")
public class Anime {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String name;
	
	@Column(name = "genres")
	private String genres;
	
	private String description;
	
	private int year;
	private double ranking;
	private String url;
	
	public Anime() {
	}
	
	public Anime(String name, List<String> genres, String description, int year, double ranking, String url) {
		this.name = name;
		this.genres = String.join(",", genres);
		this.description = description;
		this.year = year;
		this.ranking = ranking;
		this.url = url;	
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

	public List<String> getGenres() {
		return genres != null ? Arrays.asList(genres.split(",")) : new ArrayList<>();
	}

	public void setGenres(List<String> genres) {
		this.genres = String.join(",", genres);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRanking() {
		return ranking;
	}

	public void setRanking(double ranking) {
		this.ranking = ranking;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Anime{" + 
				"id=" + id + 
				", name=" + name + 
				", genres=" + genres + 
				", description=" + description + 
				", year=" + year +
				", ranking=" + ranking + 
				", url=" + url + 
				'}';
	}
}
