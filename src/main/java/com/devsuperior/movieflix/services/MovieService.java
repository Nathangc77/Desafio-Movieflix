package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.Utils.Utils;
import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.MovieReviewsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Movie entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new MovieDetailsDTO(entity);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findByGenre(String genreId, Pageable pageable) {
        Long id = null;

        if (!"0".equals(genreId)) id = Long.parseLong(genreId);

        Page<MovieProjection> page = repository.searchMovieProjectionByGenre(id, pageable);
        List<Long> ids = page.map(x -> x.getId()).toList();

        List<Movie> entities = repository.searchMovieByIds(ids);
        entities = (List<Movie>) Utils.replace(page.getContent(), entities);

        List<MovieCardDTO> dtos = entities.stream().map(MovieCardDTO::new).toList();
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public MovieReviewsDTO findMovieWithReview(Long id) {
        Movie entity = repository.searchMovieWithReviews(id);

        if (entity == null) {
            throw new ResourceNotFoundException("Entity not found");
        }

        return new MovieReviewsDTO(entity, entity.getReviews());
    }
}
