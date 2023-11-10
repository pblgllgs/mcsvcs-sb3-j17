package com.pblgllgs.album.services;

import com.pblgllgs.album.entities.AlbumEntity;

import java.util.List;

public interface AlbumService {
    List<AlbumEntity> findAllAlbumsByUserId(String userId);
    AlbumEntity save(AlbumEntity albumEntity);
}
