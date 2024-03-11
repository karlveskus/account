package com.tuum.account.mapper;

import com.tuum.account.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BalanceMapper {

    @Select("SELECT id, account_id, available_amount, currency_code FROM balance WHERE account_id=#{accountId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmount", column = "available_amount"),
            @Result(property = "currencyCode", column = "currency_code")
    })
    List<Balance> getBalancesByAccountId(@Param("accountId") UUID accountId);

    @Insert("INSERT INTO balance (id, created_at, updated_at, account_id, available_amount, currency_code) VALUES (#{id}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, #{accountId}, #{availableAmount}, #{currencyCode})")
    void insert(Balance balance);

}
