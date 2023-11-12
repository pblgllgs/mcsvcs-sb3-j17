package com.pblgllgs.users.clients;

import com.pblgllgs.users.model.responses.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums", url = "${clients.albums.url}")
public interface AlbumClient {

    @GetMapping("/users/{id}/albums")
    @Retry(name = "albums")
    @CircuitBreaker(name = "albums", fallbackMethod = "getAlbumsFallbackCircuitBreaker")
    List<AlbumResponseModel> findAllAlbums(@PathVariable String id, @RequestHeader("Authorization") String authorization);

    default List<AlbumResponseModel> getAlbumsFallbackCircuitBreaker(String id,String authorization, Throwable exception){
        System.out.println("Param = " + id);
        System.out.println("Exception took place: "+ exception.getMessage());
        return new ArrayList<>();
    }
}
