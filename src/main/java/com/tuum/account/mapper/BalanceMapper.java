package com.tuum.account.mapper;

import com.tuum.account.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BalanceMapper {

    @Select("SELECT id, account_id, available_amount_cents, currency_code " +
            "FROM balance " +
            "WHERE account_id=#{accountId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmountCents", column = "available_amount_cents"),
            @Result(property = "currencyCode", column = "currency_code")
    })
    List<Balance> getBalancesByAccountId(@Param("accountId") UUID accountId);

    @Select("SELECT id, account_id, available_amount_cents, currency_code " +
            "FROM balance " +
            "WHERE account_id=#{accountId} AND currency_code=#{currencyCode}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmountCents", column = "available_amount_cents"),
            @Result(property = "currencyCode", column = "currency_code")
    })
    Balance getBalanceByAccountIdAndCurrency(@Param("accountId") UUID accountId, @Param("currencyCode") String currencyCode);

    @Insert("INSERT INTO balance " +
            "(id, account_id, available_amount_cents, currency_code) " +
            "VALUES (#{id}, #{accountId}, #{availableAmountCents}, #{currencyCode})")
    void insert(Balance balance);

    @Update("UPDATE balance " +
            "SET available_amount_cents = #{availableAmountCents}, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{id}")
    void update(Balance balance);

}
