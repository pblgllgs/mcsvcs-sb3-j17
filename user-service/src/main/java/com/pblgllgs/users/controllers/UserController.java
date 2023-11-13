package com.pblgllgs.users.controllers;

import com.pblgllgs.users.dto.UserDto;
import com.pblgllgs.users.model.requests.CreateUserRequestModel;
import com.pblgllgs.users.model.responses.CreateUserResponseModel;
import com.pblgllgs.users.model.responses.UserResponseModel;
import com.pblgllgs.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final ServletWebServerApplicationContext applicationContext;
    private final UserService userService;
    private final Environment environment;

    @GetMapping("/status/check")
    public String status() {
        return "Users Status UP on port: " +
                applicationContext.getWebServer().getPort() + " and " +
                environment.getProperty("token.secret") + " in the " +
                environment.getProperty("message.profile") + " environment";
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> saveUser(@Valid @RequestBody CreateUserRequestModel userRequestModel) {
        UserDto userDto = mapperCreateUserRequestModelToUserDto(userRequestModel);
        UserDto userDtoSaved = userService.createUser(userDto);
        CreateUserResponseModel userResponseModel = mapperUserDtoToCreateUserResponseModel(userDtoSaved);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
    }

    private CreateUserResponseModel mapperUserDtoToCreateUserResponseModel(UserDto userDtoSaved) {
        CreateUserResponseModel createUserResponseModel = new CreateUserResponseModel();
        createUserResponseModel.setFirstName(userDtoSaved.getFirstName());
        createUserResponseModel.setLastName(userDtoSaved.getLastName());
        createUserResponseModel.setEmail(userDtoSaved.getEmail());
        createUserResponseModel.setUserId(userDtoSaved.getUserId());
        return createUserResponseModel;
    }

    private UserDto mapperCreateUserRequestModelToUserDto(CreateUserRequestModel userRequestModel) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(userRequestModel.getFirstName());
        userDto.setLastName(userRequestModel.getLastName());
        userDto.setEmail(userRequestModel.getEmail());
        userDto.setPassword(userRequestModel.getPassword());
        return userDto;
    }

    //    @PreAuthorize("principal == #userId")
//    @PostAuthorize("principal == returnObject.getBody().getUserId()")
    @PreAuthorize("hasRole('ADMIN') or principal == #userId")
    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId, @RequestHeader("Authorization") String authorization) {
        UserDto userDto = userService.findUserById(userId, authorization);
        UserResponseModel returnValue = mapperUserDtoToUserResponseModel(userDto);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    private UserResponseModel mapperUserDtoToUserResponseModel(UserDto userDto) {
        UserResponseModel responseModel = new UserResponseModel();
        responseModel.setLastName(userDto.getLastName());
        responseModel.setFirstName(userDto.getFirstName());
        responseModel.setEmail(userDto.getEmail());
        responseModel.setUserId(userDto.getUserId());
        responseModel.setAlbums(userDto.getAlbums());
        return responseModel;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PROFILE_DELETE') or principal == #userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }
}
