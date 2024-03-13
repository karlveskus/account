package com.tuum.account.dao;

import com.tuum.account.domain.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TransactionDao {

    @Insert("INSERT INTO transaction " +
            "(id, account_id, amount_cents, currency_code, direction, description) " +
            "VALUES (#{id}, #{accountId}, #{amountCents}, #{currencyCode}, #{direction}, #{description})")
    void insert(Transaction transaction);

    @Select("SELECT * " +
            "FROM transaction " +
            "WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amountCents", column = "amount_cents"),
            @Result(property = "currencyCode", column = "currency_code"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description"),
    })
    List<Transaction> getTransactionsByAccountId(@Param("accountId") UUID accountId);

}
