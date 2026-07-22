package com.ledger.application.port.out;

import com.ledger.domain.AccountId;
import com.ledger.domain.Posting;
import java.util.List;

public interface PostingRepository {
  void save(Posting posting);

  List<Posting> findByAccount(AccountId id);
}
