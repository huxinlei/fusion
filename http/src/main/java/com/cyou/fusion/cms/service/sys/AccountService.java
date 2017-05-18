package com.cyou.fusion.cms.service.sys;

import com.cyou.fusion.cms.domain.sys.Account;
import com.cyou.fusion.cms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountService
 * <p>
 * Created by zhanglei_js on 2017/5/18.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account login(String email, String password) {
        return accountRepository.findByEmailAndPassword(email, password);
    }
}
