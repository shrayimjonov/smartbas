package uz.davrbank.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.request.AuthRequest;
import uz.davrbank.dto.request.ChangePasswordDto;
import uz.davrbank.dto.request.ChangePasswordRequestDto;
import uz.davrbank.dto.request.RefreshRequest;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.dto.response.TokenResponse;
import uz.davrbank.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ResultData<TokenResponse>> login(@Valid @RequestBody AuthRequest req) {
        var result = userService.login(req);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResultData<TokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        var result = userService.refreshToken(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDto dto) {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto().fromRequest(dto, userDetails.getUsername());
        var result = userService.changePassword(changePasswordDto);
        return ResponseEntity.ok(result);
    }

}
