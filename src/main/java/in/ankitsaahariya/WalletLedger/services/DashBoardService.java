package in.ankitsaahariya.WalletLedger.services;

import in.ankitsaahariya.WalletLedger.dto.ExpenseDTO;
import in.ankitsaahariya.WalletLedger.dto.IncomeDTO;
import in.ankitsaahariya.WalletLedger.dto.RecentTransactionDTO;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private  final  ProfileService profileService;

    public Map<String,Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransaction = concat(latestIncomes.stream().map(income ->
                RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()),
                latestExpense.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(expense.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()))
                .sorted((a,b)-> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }

                    return cmp;
                }).collect(Collectors.toUnmodifiableList());

        returnValue.put("totalBalance",incomeService.getTotalIncomesForCurrentUser().
                subtract(expenseService.getTotalExpenseForCurrentUser()));

        returnValue.put("totalIncome",incomeService.getTotalIncomesForCurrentUser());
        returnValue.put("totalExpense",expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("recent5Expenses",latestExpense);
        returnValue.put("recent5Incomes",latestIncomes);
        returnValue.put("recentTransactions",recentTransaction);
        return returnValue;
    }
}
