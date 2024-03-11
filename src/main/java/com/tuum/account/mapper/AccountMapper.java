package com.tuum.account.mapper;

import com.tuum.account.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface AccountMapper {
    @Select("SELECT id FROM account WHERE id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "id")
    })
    Account getAccount(@Param("id") UUID id);

    @Insert("INSERT INTO account (id, created_at, updated_at, customer_id, country) VALUES (#{id}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, #{customerId}, #{country})")
    void save(Account account);
}
