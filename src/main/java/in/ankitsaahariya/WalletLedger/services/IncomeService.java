package in.ankitsaahariya.WalletLedger.services;

import in.ankitsaahariya.WalletLedger.dto.ExpenseDTO;
import in.ankitsaahariya.WalletLedger.dto.IncomeDTO;
import in.ankitsaahariya.WalletLedger.entity.CategoryEntity;
import in.ankitsaahariya.WalletLedger.entity.ExpenseEntity;
import in.ankitsaahariya.WalletLedger.entity.IncomeEntity;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import in.ankitsaahariya.WalletLedger.repository.CategoryRepository;
import in.ankitsaahariya.WalletLedger.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;


    //    Add a new income to the database
    public IncomeDTO addIncomes(IncomeDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        IncomeEntity newExpense = toEntity(dto,profile,category);
        newExpense=incomeRepository.save(newExpense);
        return toDTO(newExpense);
    }

//    Retrieves all expenses for the current Month  / based on start date and current date
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDTO).toList();
    }

    //   Delete Income by id for current user
    public  void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(()->  new RuntimeException("Income not found"));
        if (!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this Income");
        }
        incomeRepository.delete(entity);
    }

    //    Get Latest 5 Incomes for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    //    Get total incomes for current user
    public BigDecimal getTotalIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total!= null ? total : BigDecimal.ZERO;
    }

    //    Filter Incomes
    public  List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword , Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return  list.stream().map(this::toDTO).toList();
    }

    //    helper
    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity){
        return IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId():null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName():"N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
