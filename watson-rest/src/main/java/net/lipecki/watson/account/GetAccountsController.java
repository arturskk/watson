package net.lipecki.watson.account;

import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Api.V1)
public class GetAccountsController {

    private final GetAccountsQuery query;

    public GetAccountsController(final GetAccountsQuery query) {
        this.query = query;
    }

    @GetMapping("/account")
    @Transactional
    public List<Account> getAccounts() {
        return this.query.getAccounts();
    }

}
