package in.ankitsaahariya.WalletLedger.controller;

import in.ankitsaahariya.WalletLedger.dto.ExpenseDTO;
import in.ankitsaahariya.WalletLedger.dto.IncomeDTO;
import in.ankitsaahariya.WalletLedger.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Incomes")
public class IncomeController {

    private final  IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncomes(@RequestBody IncomeDTO dto){
        IncomeDTO saved = incomeService.addIncomes(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getExpenses(){
        List<IncomeDTO>  incomes = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id){
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
