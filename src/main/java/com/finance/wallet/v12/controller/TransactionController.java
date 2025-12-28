package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
import com.finance.wallet.v12.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/transations")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody TransactionDepositDTO transactionDepositDTO,
                                                          UriComponentsBuilder uriComponentsBuilder)
    {
        TransactionResponseDTO deposit = this.transactionService.deposit(transactionDepositDTO);
        URI uri = uriComponentsBuilder
                .path("/deposit/{id}")
                .buildAndExpand(deposit.id())
                .toUri();
        return ResponseEntity.created(uri).body(deposit);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody TransactionTransferDTO transferDTO,
                                                           UriComponentsBuilder uriComponentsBuilder,
                                                           @AuthenticationPrincipal User loggedUser)
    {
        TransactionResponseDTO transfer = this.transactionService.transfer(transferDTO, loggedUser);
        URI uri = uriComponentsBuilder
                .path("/tranfer/{id}")
                .buildAndExpand(transfer.id())
                .toUri();
        return ResponseEntity.created(uri).body(transfer);
    }

    @GetMapping("/bankstatement")
    public ResponseEntity<Page<TransactionResponseDTO>> bankStatement(@RequestParam UUID walletId, @PageableDefault(size = 20, page = 0,
    sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal User loggedUser)
    {
        Page<TransactionResponseDTO> bankStatement = this.transactionService.bankStatement(walletId, pageable, loggedUser);
        return new ResponseEntity<>(bankStatement,HttpStatus.OK);
    }
}
