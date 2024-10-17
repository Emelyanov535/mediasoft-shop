package ru.mediasoft.shop.service.account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AccountServiceClient {
    CompletableFuture<Map<String, String>> getAccountsNumber(List<String> logins);
}
