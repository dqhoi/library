package com.app.library.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.app.library.dto.*;
import com.app.library.model.*;
import com.app.library.repository.*;
import com.app.library.service.*;
import com.app.library.utils.SecurityUtil;

@Service
public class LoanServiceImpl implements com.app.library.service.ILoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IBookService bookService;

    @Override
    public ResponseEntity<?> searchLoan(int Id) {
        Loan loan = loanRepository.findById(Id).orElseThrow(() -> new RuntimeException("can't find loan id:" + Id));
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> listUserBorrowing() {
        List<Loan> loans = loanRepository.findAll();
        List<User> users = loans.stream().map(Loan::getUser).collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteLoan(int Id) {
        Loan loan = loanRepository.findById(Id).orElseThrow(() -> new RuntimeException("can't find loan id:" + Id));
        List<Book> bookList = loan.getBooks();
        for (Book book : bookList) {
            int bookId = book.getBookId();
            // Lấy số lượng sách hiện tại trong bookRepository và cộng với số sách hiện tại
            // trong loan tương ứng với id
            int currentQuantity = bookRepository.findById(bookId).get().getBookQuantity();
            int loanBookQuantity = book.getBookQuantity();
            int totalQuantity = currentQuantity + loanBookQuantity;
            // Cập nhật lại số lượng sách trong bookRepository
            bookRepository.findById(bookId).get().setBookQuantity(totalQuantity);
        }
        loanRepository.deleteById(Id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateLoan(int Id, LoanDto newLoan) {
        // if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")) {

            Optional<Loan> loan = loanRepository.findById(Id);
            if (!loan.isPresent()) {
                throw new RuntimeException("Cannot find loan with id: " + Id);
            }

            Loan loan1 = loan.get();

            // Update loan properties
            loan1.setLoanDueDate(newLoan.getLoanDueDate());

            // Update by user with date
            loan1.setUpdatedAt(LocalDateTime.now());
            loan1.setUpdatedBy(null);

            User user = loan1.getUser();
            // kiểm tra xem newLoan.getUser() có tồn tại không
            if (newLoan.getUser().getUserId() == user.getUserId()) {
                if (newLoan.getUser().getUsername() != user.getUsername()) {
                    user.setUsername(newLoan.getUser().getUsername());
                }
                if (newLoan.getUser().getEmail() != user.getEmail()) {
                    user.setEmail(newLoan.getUser().getEmail());
                }
                if (newLoan.getUser().getFirstName() != user.getFirstName()) {
                    user.setFirstName(newLoan.getUser().getFirstName());
                }
                if (newLoan.getUser().getLastName() != user.getLastName()) {
                    user.setLastName(newLoan.getUser().getLastName());
                }
                if (newLoan.getUser().getAddress() != user.getAddress()) {
                    user.setAddress(newLoan.getUser().getAddress());
                }
                if (newLoan.getUser().getAvatarUrl() != user.getAvatarUrl()) {
                    user.setAvatarUrl(newLoan.getUser().getAvatarUrl());
                }
                userRepository.save(user);
            } else {
                throw new RuntimeException("Cannot update user with id: " + newLoan.getUser().getUserId());
            }

            List<Book> books = loan1.getBooks();
            for (Book bookLoan : books) {
                for (BookDto booknewLoan : newLoan.getBooks()) {
                    if (booknewLoan.getBookId() == bookLoan.getBookId()) {
                        int totalQuantity = bookLoan.getBookQuantity()
                                + bookRepository.findById(booknewLoan.getBookId()).get().getBookQuantity();
                        if (booknewLoan.getBookQuantity() < totalQuantity) {
                            bookLoan.setBookQuantity(booknewLoan.getBookQuantity());
                            bookRepository.findById(bookLoan.getBookId()).get().setBookQuantity(totalQuantity
                                    - booknewLoan.getBookQuantity());
                        } else {
                            throw new RuntimeException("Cannot update book with id: " + booknewLoan.getBookId());
                        }
                    } else {
                        books.add(bookRepository.findById(booknewLoan.getBookId())
                            .orElseThrow(() -> new RuntimeException("Cannot find book with id: " + booknewLoan.getBookId())));
                        bookLoan.setBookId(booknewLoan.getBookId());
                        bookLoan.setBookQuantity(booknewLoan.getBookQuantity());
                        bookLoan.setBookTitle(booknewLoan.getBookTitle());
                        bookRepository.findById(booknewLoan.getBookId()).get().setBookQuantity(
                                bookRepository.findById(booknewLoan.getBookId()).get().getBookQuantity()
                                        - booknewLoan.getBookQuantity());
                    }
                }
            }

            loanRepository.save(loan1);
            return new ResponseEntity<>(loan1, HttpStatus.OK);
        //}
        // return null;
    }

    @Override
    public ResponseEntity<?> addLoan(int Id) {
        if (!loanRepository.findById(Id).isPresent()) {
            Loan newLoan = new Loan();
            newLoan.setLoanId(Id);
            loanRepository.save(newLoan);
            return new ResponseEntity<>(newLoan, HttpStatus.OK);
        }
        return null;
    }

}