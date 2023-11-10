package com.pblgllgs.album.services.impl;

import com.pblgllgs.album.entities.AlbumEntity;
import com.pblgllgs.album.repositories.AlbumRepository;
import com.pblgllgs.album.services.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    @Override
    public List<AlbumEntity> findAllAlbumsByUserId(String userId) {
        return albumRepository.findAllByUserId(userId);
    }

    @Override
    public AlbumEntity save(AlbumEntity albumEntity) {
        return albumRepository.save(albumEntity);
    }


}
