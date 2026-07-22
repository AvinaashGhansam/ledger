package com.ledger.adapter.persistence;

import com.ledger.application.port.out.PostingRepository;
import com.ledger.domain.AccountId;
import com.ledger.domain.Posting;
import com.ledger.domain.PostingId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPostingRepository implements PostingRepository {
  private final Map<PostingId, Posting> store = new ConcurrentHashMap<>();

  @Override
  public void save(Posting posting) {
    store.put(posting.id(), posting);
  }

  @Override
  public List<Posting> findByAccount(AccountId id) {
    return store.values().stream()
        .filter(posting -> posting.entries().stream().anyMatch(entry -> entry.account().equals(id)))
        .collect(Collectors.toList());
  }
}
