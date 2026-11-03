package soap.humo_gateway.service;

public interface HumoIntegrationClientService {
    <B, R> R sendPostRequest(B body, Class<R> clazz, String pathURI);

    <R> R sendGetRequest(Class<R> clazz, String pathURI);
}
