package net.lipecki.watson.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAccountsController {

    @GetMapping("/account")
    public List<Account> getAccounts() {
        return new ArrayList<>();
    }

}
