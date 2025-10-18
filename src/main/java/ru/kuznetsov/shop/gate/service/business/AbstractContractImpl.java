package ru.kuznetsov.shop.gate.service.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.kuznetsov.shop.gate.exceptions.ServiceException;
import ru.kuznetsov.shop.represent.contract.business.AbstractContract;
import ru.kuznetsov.shop.represent.dto.AbstractDto;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public abstract class AbstractContractImpl<E extends AbstractDto> implements AbstractContract<E> {

    @Value("${web.client.retry}")
    private long RETRY;
    @Value("${web.client.max-attempts}")
    private int MAX_ATTEMPTS;

    private final Class<E> clazz;
    protected final WebClient webClient;

    protected AbstractContractImpl(WebClient webClient) {
        this.webClient = webClient;
        this.clazz = ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected abstract String getModuleName();

    @Override
    public E getById(Long id) {
        return sendGetRequest(getModuleName() + "/" + id, null, null, clazz).get(0);
    }

    @Override
    public List<E> getAll() {
        return sendGetRequest(getModuleName(), null, null, clazz);
    }

    @Override
    public boolean create(E entity) {
        return sendPostRequest(getModuleName(), null, entity, Boolean.class).get(0);
    }

    @Override
    public E update(E entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        sendDeleteRequest(getModuleName() + "/" + id, null, null, clazz);
    }

    protected <T> List<T> sendGetRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        return sendRequest(HttpMethod.GET, uri, queryParams, body, clazz);
    }

    protected <T> List<T> sendPostRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        return sendRequest(HttpMethod.POST, uri, queryParams, body, clazz);
    }

    protected <T> void sendDeleteRequest(String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        sendRequest(HttpMethod.DELETE, uri, queryParams, body, clazz);
    }

    private <T> List<T> sendRequest(HttpMethod method, String uri, Map<String, ?> queryParams, Object body, Class<T> clazz) {
        Mono<List<T>> mono = webClient.method(method)
                .uri(uriBuilder -> {
                    uriBuilder.path(uri);
                    for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                        uriBuilder.queryParam(entry.getKey(), entry.getValue());
                    }
                    return uriBuilder.build();
                })
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    System.out.println("Логирование 4** ошибок");
                    return response.bodyToMono(String.class).map(RuntimeException::new);
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    System.out.println("Логирование 5** ошибок");
                    return Mono.empty();
                })
                .bodyToFlux(clazz)
                .retryWhen(Retry.backoff(MAX_ATTEMPTS, Duration.ofMillis(RETRY))
                        .jitter(0.75)
                        .doBeforeRetry(x -> System.out.println("Повторная попытка отправки " + x.totalRetries()))
                        .filter(throwable -> throwable instanceof ServiceException
                                || throwable instanceof WebClientRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("Повторные попытки исчерпаны. Внешний сервис недоступен.",
                                    HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .onErrorResume(ServiceException.class, err -> {
                    System.out.println("Код ошибки: " + err.getStatusCode());
                    err.printStackTrace();
                    return Mono.empty();
                })
                .collectList();
        return mono.block();
    }
}
