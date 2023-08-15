package com.app.library.dto;

import com.app.library.model.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Loan}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto implements Serializable {
    int loanId;
    LocalDateTime loanDueDate;
    UserDto user;
    List <BookDto> books;
}