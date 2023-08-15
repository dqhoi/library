package com.app.library.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.app.library.dto.LoanDto;
import com.app.library.model.User;

public interface ILoanService {
    public ResponseEntity<?> searchLoan(int Id);

    public ResponseEntity<List<User>> listUserBorrowing();

    public ResponseEntity<?> deleteLoan(int Id);

    public ResponseEntity<?> updateLoan(int Id, LoanDto newLoan);

    public ResponseEntity<?> addLoan();

}
