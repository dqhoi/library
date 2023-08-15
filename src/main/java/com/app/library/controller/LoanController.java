package com.app.library.controller;

import com.app.library.dto.LoanDto;
import com.app.library.model.*;
import com.app.library.service.impl.LoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanServiceImpl loanService;

    @GetMapping("/info/{id}")
    public ResponseEntity<?> searchLoanById(@PathVariable("id") int id) {
        return loanService.searchLoan(id);
    }

    @GetMapping(value = "/listUserBorrowing")
    public ResponseEntity<List<User>> getUserBorrowingById() {
        return loanService.listUserBorrowing();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable("id") int id) {
        return loanService.deleteLoan(id);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable("id") int id, @RequestBody LoanDto newLoan) {
        ResponseEntity<?> loan = loanService.updateLoan(id, newLoan);
        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>((Loan) loan.getBody(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/{id}")
    public ResponseEntity<?> addLoan(@PathVariable("id") int id) {
        ResponseEntity<?> loan = loanService.addLoan(id);
        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }
}
