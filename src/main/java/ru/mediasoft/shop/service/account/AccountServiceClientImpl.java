package ru.mediasoft.shop.service.account;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mediasoft.shop.configuration.WebConfig;
import ru.mediasoft.shop.configuration.properties.RestConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AccountServiceClientImpl implements AccountServiceClient {

    private final RestConfig restConfig;
    private final WebConfig webConfig;

    @Override
    public CompletableFuture<Map<String, String>> getAccountsNumber(List<String> logins) {
        WebClient webClient = webConfig.getAccountClient();
        return webClient.post()
                .uri(restConfig.getAccount().getMethods().getGetAccountNumber())
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .toFuture();
    }
}
