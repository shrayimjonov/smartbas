package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import soap.humo_gateway.entity.ErrorList;
import soap.humo_gateway.repository.ErrorListRepository;
import soap.humo_gateway.service.ErrorListService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ErrorListImpl implements ErrorListService {
    private final ErrorListRepository repository;

    @Override
    @Cacheable(value = "errorList")
    public List<ErrorList> findAllErrors() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "errorByCode")
    public String findErrorByCode(String code) {
        if (code.chars().allMatch(Character::isDigit))
            return repository.findByCode(code).getMessage();
        return code;
    }
}
