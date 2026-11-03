package uz.davrbank.educontrolservice.service.dao.security;

import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.RefreshTokenRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;

import java.util.List;

public interface UserInterface {

    Token login(LoginRequest loginRequest);

    Token refreshToken(RefreshTokenRequest refreshRequest);

    UserInfoResponse getUserInfo();

    List<Object> getAllUsers();
}
