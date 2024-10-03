package ru.mediasoft.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mediasoft.shop.service.account.AccountServiceClient;
import ru.mediasoft.shop.service.crm.CrmServiceClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AccountDataProvider {
    private final CrmServiceClient crmServiceClient;
    private final AccountServiceClient accountServiceClient;

    public CompletableFuture<Map<String, String>> fetchAccountNumber(List<String> logins) {
        return accountServiceClient.getAccountsNumber(logins);
    }

    public CompletableFuture<Map<String, String>> fetchAccountInn(List<String> logins) {
        return crmServiceClient.getAccountsInn(logins);
    }
}
