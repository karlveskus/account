package com.tuum.account.dao;

import com.tuum.account.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface AccountDao {

    @Select("SELECT id, customer_id " +
            "FROM account " +
            "WHERE id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id")
    })
    Account getAccount(@Param("id") UUID id);

    @Insert("INSERT INTO account " +
            "(id, customer_id, country) " +
            "VALUES (#{id}, #{customerId}, #{country})")
    void insert(Account account);

}
