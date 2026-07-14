package com.ledger.domain;

import java.util.UUID;

public sealed interface LedgerId permits AccountId {
  UUID value();
}
