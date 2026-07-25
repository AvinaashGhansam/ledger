package com.ledger.application.service;

import com.ledger.application.port.in.PostTransactionCommand;
import com.ledger.application.port.in.PostTransactionError;
import com.ledger.application.port.in.PostTransactionUseCase;
import com.ledger.application.port.out.AccountRepository;
import com.ledger.application.port.out.IdGenerator;
import com.ledger.application.port.out.PostingRepository;
import com.ledger.domain.Account;
import com.ledger.domain.AccountId;
import com.ledger.domain.Entry;
import com.ledger.domain.Posting;
import com.ledger.domain.PostingError;
import com.ledger.domain.PostingId;
import com.ledger.domain.Result;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PostTransactionService implements PostTransactionUseCase {
  private final AccountRepository accountRepository;
  private final PostingRepository postingRepository;
  private final IdGenerator idGenerator;
  private final Clock clock;

  public PostTransactionService(
      AccountRepository accountRepository,
      PostingRepository postingRepository,
      IdGenerator idGenerator,
      Clock clock) {
    this.accountRepository = accountRepository;
    this.postingRepository = postingRepository;
    this.idGenerator = idGenerator;
    this.clock = clock;
  }

  @Override
  public Result<Posting, PostTransactionError> post(PostTransactionCommand command) {

    Map<AccountId, Account> resolvedAccounts = new HashMap<>();

    for (PostTransactionCommand.Line line : command.lines()) {
      Optional<Account> accountOpt = accountRepository.findById(line.accountId());

      if (accountOpt.isEmpty()) {
        return Result.err(new PostTransactionError.UnknownAccount(line.accountId()));
      }
      resolvedAccounts.put(line.accountId(), accountOpt.get());
    }

    List<Entry> entries = new ArrayList<>();

    for (PostTransactionCommand.Line line : command.lines()) {
      Account account = resolvedAccounts.get(line.accountId());

      if (!account.currency().equals(line.amount().currency())) {
        return Result.err(
            new PostTransactionError.AccountCurrencyMismatch(
                line.accountId(), account.currency(), line.amount().currency()));
      }
      entries.add(new Entry(line.accountId(), line.amount()));
    }

    PostingId id = idGenerator.nextPostingId();
    Instant currentTime = clock.instant();

    Result<Posting, PostingError> domainResult =
        Posting.create(id, currentTime, command.description(), entries);

    return switch (domainResult) {
      case Result.Ok<Posting, PostingError> ok -> {
        Posting validPosting = ok.value();
        postingRepository.save(validPosting);
        yield Result.ok(validPosting);
      }
      case Result.Err<Posting, PostingError> err -> {
        yield Result.err(new PostTransactionError.InvalidPosting(err.error()));
      }
    };
  }
}
