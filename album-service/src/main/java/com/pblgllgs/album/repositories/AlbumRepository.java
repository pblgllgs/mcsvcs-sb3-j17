package com.pblgllgs.album.repositories;

import com.pblgllgs.album.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<AlbumEntity,Long> {
    List<AlbumEntity> findAllByUserId(String id);
}
