package com.condigence.searchservice.service;


import com.condigence.searchservice.entity.MovieShow;
import com.condigence.searchservice.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	ShopRepository repository;

	public static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);


	@Override
	public List<MovieShow> searchByTitle() {
		return null;
	}

	@Override
	public List<MovieShow> searchByReleaseDate() {
		return null;
	}

	@Override
	public List<MovieShow> searchByCity() {
		return null;
	}

	@Override
	public List<MovieShow> searchByLanguage() {
		return null;
	}

	@Override
	public List<MovieShow> searchByGenre() {
		return null;
	}
}
