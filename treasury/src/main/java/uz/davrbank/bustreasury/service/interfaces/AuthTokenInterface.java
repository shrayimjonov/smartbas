package uz.davrbank.bustreasury.service.interfaces;


import uz.davrbank.bustreasury.aggregate.dto.auth.AuthTokenDto;

public interface AuthTokenInterface {
    AuthTokenDto getAuthToken();
}