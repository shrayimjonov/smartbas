package uz.davrbank.service;

import jakarta.validation.Valid;
import uz.davrbank.dto.request.AuthRequest;
import uz.davrbank.dto.request.ChangePasswordDto;
import uz.davrbank.dto.request.RefreshRequest;
import uz.davrbank.dto.request.UserRequestDto;
import uz.davrbank.dto.response.DefaultSuccessResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.dto.response.TokenResponse;
import uz.davrbank.dto.response.UserResponse;

public interface UserService {
    ResultData<UserResponse> createUser(UserRequestDto userRequestDto);

    ResultData<TokenResponse> login(AuthRequest dto);

    ResultData<TokenResponse> refreshToken(RefreshRequest dto);

    ResultData<DefaultSuccessResponse> changePassword(@Valid ChangePasswordDto dto);

}
