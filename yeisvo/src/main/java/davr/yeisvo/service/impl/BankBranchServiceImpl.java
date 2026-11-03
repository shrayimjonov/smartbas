package davr.yeisvo.service.impl;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import davr.yeisvo.dto.request.BankBranchRequest;
import davr.yeisvo.dto.response.BankBranchResponse;
import davr.yeisvo.entity.postgres.BankBranchEntity;
import davr.yeisvo.exceptionTemp.DuplicateFieldException;
import davr.yeisvo.exceptionTemp.GlobalExceptionMessage;
import davr.yeisvo.mapper.BankBranchMapper;
import davr.yeisvo.repository.postgres.BankBranchRepository;
import davr.yeisvo.service.BankBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@AutoJsonRpcServiceImpl
public class BankBranchServiceImpl implements BankBranchService {

    private final BankBranchRepository bankBranchRepository;
    private final BankBranchMapper bankBranchMapper;

    @Override
    public BankBranchResponse create(BankBranchRequest request) {
        BankBranchResponse response = new BankBranchResponse();
        if (bankBranchRepository.existsByMfoAndInn(request.getMfo(), request.getInn())) {
            response.setStatus(0);
            response.setErrorMsg("This bank branch already exists");
            return response;
        }

        BankBranchEntity entity = BankBranchMapper.toEntity(request);
        entity.setIsActive(true);
        response = BankBranchMapper.toResponse(bankBranchRepository.save(entity));
        response.setStatus(0);
        response.setErrorMsg("OK");
        return response;
    }

    @Override
    public BankBranchResponse update(BankBranchRequest request) {
        Optional<BankBranchEntity> entity = bankBranchRepository.findById(request.getId());
        BankBranchResponse response = new BankBranchResponse();
        if (entity.isEmpty()) {
            response.setErrorMsg("No such branch exists!");
            response.setStatus(0);
            return response;
        }
        entity.get().setMfo(request.getMfo());
        entity.get().setInn(request.getInn());
        entity.get().setName(request.getName());
        response = BankBranchMapper.toResponse(bankBranchRepository.save(entity.get()));
        response.setStatus(0);
        response.setErrorMsg("OK");
        return response;
    }


    @Override
    public BankBranchResponse delete(Long id) {
        Optional<BankBranchEntity> entity = bankBranchRepository.findById(id);

        BankBranchResponse response = new BankBranchResponse();
        if (entity.isEmpty()) {
            response.setErrorMsg("No such branch exists!");
            response.setStatus(0);
            return response;
        }

        entity.get().setIsActive(false);
        response = BankBranchMapper.toResponse(bankBranchRepository.save(entity.get()));

        return response;
    }

    public List<BankBranchEntity> allBranches() {
        return bankBranchRepository.findAll();
    }

    @Override
    public List<BankBranchResponse> getAll() {
        return bankBranchRepository.findAll().stream()
                .map(entity -> {
                    BankBranchResponse response = BankBranchMapper.toResponse(entity);
                    response.setErrorMsg("OK");
                    response.setStatus(0);
                    return response;
                })
                .toList();
    }

    @Override
    public BankBranchResponse getById(Long id) {
        BankBranchResponse res = new BankBranchResponse();
        Optional<BankBranchEntity> response = bankBranchRepository.findById(id);
        if (response.isEmpty()) {
            res.setStatus(0);
            res.setErrorMsg("Branch not found!");
            return res;
        }
        res = BankBranchMapper.toResponse(response.get());
        res.setStatus(0);
        res.setErrorMsg("OK");
        return res;
    }
}


