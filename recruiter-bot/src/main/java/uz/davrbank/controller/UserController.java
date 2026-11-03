package uz.davrbank.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.dto.request.UserRequestDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.dto.response.UserResponse;
import uz.davrbank.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResultData<UserResponse>> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        var resultData = userService.createUser(userRequestDto);
        return ResponseEntity.ok(resultData);
    }

}
