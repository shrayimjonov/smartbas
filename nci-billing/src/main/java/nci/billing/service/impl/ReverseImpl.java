package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import nci.billing.entity.Reverse;
import nci.billing.exception.EntityNotFound;
import nci.billing.model.enums.PayStatus;
import nci.billing.repository.ReverseRepository;
import nci.billing.service.ReverseService;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReverseImpl implements ReverseService {
    private final ReverseRepository repository;

    @Override
    public Reverse create(Map<String, String> params) {
        Reverse reverse = new Reverse();
        reverse.setStatus(PayStatus.CREATED);
        reverse.setRequest(params);
        return repository.save(reverse);
    }

    @Override
    public void update(Long id, String response, PayStatus status) {
        var entity = findById(id);
        entity.setUpdatedTime(ZonedDateTime.now(ZoneId.of("Asia/Tashkent")));
        entity.setStatus(status);
        entity.setResponse(response);
        repository.save(entity);
    }

    @Override
    public Reverse findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Reverse entity with id %d not found in database".formatted(id)));
    }
}
