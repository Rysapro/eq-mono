package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.model.Window;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WindowRepository extends BaseRepository<Window> {
    @Query("SELECT w FROM Window w WHERE w.place.id = :placeId")
    List<Window> findByPlaceId(@Param("placeId") Long placeId);


}
