package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
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
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody TransactionDepositDTO transactionDepositDTO,
                                                          UriComponentsBuilder uriComponentsBuilder)
    {
        TransactionResponseDTO deposit = this.transactionService.deposit(transactionDepositDTO);
        URI uri = uriComponentsBuilder
                .path("/transaction/{id}")
                .buildAndExpand(deposit.id())
                .toUri();
        return ResponseEntity.created(uri).body(deposit);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody TransactionTransferDTO transferDTO,
                                                           UriComponentsBuilder uriComponentsBuilder)
    {
        TransactionResponseDTO transfer = this.transactionService.transfer(transferDTO);
        URI uri = uriComponentsBuilder
                .path("/transaction/{id}")
                .buildAndExpand(transfer.id())
                .toUri();
        return ResponseEntity.created(uri).body(transfer);
    }
}
