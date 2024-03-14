package com.tuum.account.service;

import org.apache.logging.log4j.util.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionRunner {
    @Transactional
    public <T> T runInTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

}
