package com.ledger.application.port.in;

import com.ledger.domain.Currency;

public record OpenAccountCommand(String name, Currency currency) {}
