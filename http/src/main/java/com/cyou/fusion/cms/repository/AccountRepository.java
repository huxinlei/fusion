package com.cyou.fusion.cms.repository;

import com.cyou.fusion.cms.domain.sys.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * AccountRepository
 * <p>
 * Created by zhanglei_js on 2017/5/18.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByEmailAndPassword(String email, String password);
}
