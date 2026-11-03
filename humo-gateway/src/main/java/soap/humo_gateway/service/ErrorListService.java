package soap.humo_gateway.service;

import soap.humo_gateway.entity.ErrorList;

import java.util.List;

public interface ErrorListService {
    List<ErrorList> findAllErrors();

    String findErrorByCode(String code);

}
