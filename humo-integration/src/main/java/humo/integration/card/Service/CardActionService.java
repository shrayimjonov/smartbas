package humo.integration.card.Service;

import org.springframework.stereotype.Service;

@Service
public interface CardActionService {

     <T> void createNewAction(T obj, String method, Long logProtocolId);
}
