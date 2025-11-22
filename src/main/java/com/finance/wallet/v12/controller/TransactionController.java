package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.response.TransactionDepositResponseDTO;
import com.finance.wallet.v12.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/transations")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDepositResponseDTO> deposit(@RequestBody TransactionDepositDTO transactionDepositDTO,
                                                                 UriComponentsBuilder uriComponentsBuilder)
    {
        TransactionDepositResponseDTO transaction = this.transactionService.deposit(transactionDepositDTO);
        URI uri = uriComponentsBuilder
                .path("/transaction/{id}")
                .buildAndExpand(transaction.id())
                .toUri();
        return ResponseEntity.created(uri).body(transaction);
    }
}
