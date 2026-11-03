package humo.integration.card.Service;


public interface LimitCardService {
    Object getLimitBalance(Object request);
    Object getRefData();
    Object setAnyLimit(Object request);
    Object editAnyLimit(Object request);
    Object getSubjectValue(Object request);
    Object deleteLimit(Object request);
    Object deleteAllLimit(Object request);

}
