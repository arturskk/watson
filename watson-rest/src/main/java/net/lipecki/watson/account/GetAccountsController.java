package net.lipecki.watson.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAccountsController {

    private final GetAccountsQuery query;

    public GetAccountsController(final GetAccountsQuery query) {
        this.query = query;
    }

    @GetMapping("/account")
    public List<Account> getAccounts() {
        return this.query.getAccounts();
    }

}
