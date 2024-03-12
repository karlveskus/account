package com.tuum.account.mapper;

import com.tuum.account.domain.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transaction (id, account_id, amount, direction, description) " +
            "VALUES (#{id}, #{accountId}, #{amount}, #{direction}, #{description})")
    void insert(Transaction transaction);

}
