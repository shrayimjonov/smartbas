package davr.uzcard.Service;

import davr.uzcard.Entity.Params;
import davr.uzcard.Exception.NotFoundException;
import davr.uzcard.Model.ParamDto;
import davr.uzcard.Repository.ServiceParamsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParamsService {
    private final ServiceParamsRepository repository;
    private final DecryptService decryptService;


    public String getValue(String name) {
        log.info("Getting value for : {}", name);
        return repository.getParam(name)
                .orElseThrow(() -> new NotFoundException(String.format("Param with name %s not found", name))).getValue();
    }


    public Params addParam(ParamDto dto) {
        log.info("Add new value : {}", dto);
        var value = decryptService.encrypt(dto.getValue());
        return repository.save(new Params(dto.getName(), value));
    }
}
