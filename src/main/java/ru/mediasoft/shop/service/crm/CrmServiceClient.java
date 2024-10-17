package ru.mediasoft.shop.service.crm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CrmServiceClient {
    CompletableFuture<Map<String, String>> getAccountsInn(List<String> logins);
}
