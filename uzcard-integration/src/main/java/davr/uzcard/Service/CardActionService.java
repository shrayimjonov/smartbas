package davr.uzcard.Service;

import davr.uzcard.Entity.CardAction;
import org.springframework.stereotype.Service;

@Service
public interface CardActionService {

     <T> void createNewAction(T obj, String method, Long logProtocolId);
}
