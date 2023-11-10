package com.pblgllgs.users.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final Environment environment;
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                //something
                break;
            case 404: {
                if (s.contains("getAlbums")) {
                    return new ResponseStatusException(
                            HttpStatus.valueOf(response.status()),
                            environment.getProperty("albums.exception")
                    );
                }
                break;
            }
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
