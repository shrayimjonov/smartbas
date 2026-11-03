package uz.davrbank.userinfobase.service;

import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;
import org.springframework.stereotype.Service;
import uz.davrbank.userinfobase.exceptions.GeneralException;

@Service
@RequiredArgsConstructor
public class HashIdService {

    private final Hashids hashids;

    public HashIdService() {
        this.hashids = new Hashids("davrhub-secret-key", 8);
    }

    public String encode(Long id) {
        return hashids.encode(id);
    }

    public Long decode(String hashId) {
        long[] decoded = hashids.decode(hashId);

        if (decoded.length == 0) {
            throw new GeneralException("Invalid hash id");
        }

        return decoded[0];
    }
}