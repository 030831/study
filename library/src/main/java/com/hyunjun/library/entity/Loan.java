package com.hyunjun.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "loan")
    private List<LoanItem> loanItems = new ArrayList<>();

    private LocalDate loanDate;

    private LocalDate dueDate;

    private static final int LOAN_PERIOD_DAYS = 14;

    public Loan(Member member, LocalDate loanDate) {
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
    }

    protected Loan() {

    }
}
