package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


    @Query(nativeQuery = true, value = """
    SELECT * FROM (
    SELECT tb_movie.id, tb_movie.title
    FROM tb_movie
    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
    ORDER BY tb_movie.title
    ) tb_result
    """, countQuery = """
    SELECT COUNT(*) FROM (
    SELECT tb_movie.id, tb_movie.title
    FROM tb_movie
    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
    ORDER BY tb_movie.title
    ) tb_result
    """)
    Page<MovieProjection> searchMovieProjectionByGenre(Long genreId, Pageable pageable);

    @Query("SELECT obj FROM Movie obj WHERE obj.id IN :ids")
    List<Movie> searchMovieByIds(List<Long> ids);
}
