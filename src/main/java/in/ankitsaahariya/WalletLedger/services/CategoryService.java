package in.ankitsaahariya.WalletLedger.services;

import in.ankitsaahariya.WalletLedger.dto.CategoryDTO;
import in.ankitsaahariya.WalletLedger.entity.CategoryEntity;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import in.ankitsaahariya.WalletLedger.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;


//    save category
    public  CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())) {
            throw new RuntimeException("Category with this name already exists");
        }
        CategoryEntity newCategoryEntity = toEntity(categoryDTO,profile);
        newCategoryEntity = categoryRepository.save(newCategoryEntity);
        return toDto(newCategoryEntity);
    }

//    get Category for current user
    public List<CategoryDTO> getCategoriesCurrentUser(){
        ProfileEntity profile =  profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDto).toList();
    }

//    Get categories by type for current users
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return entities.stream().map(this::toDto).toList();
    }

//    Update categories
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(()-> new RuntimeException("Category not found !"));
        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory = categoryRepository.save(existingCategory);

        return toDto(existingCategory);
    }

//    helper method (convert dto data to entity
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDto(CategoryEntity entity){
        return CategoryDTO.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() != null ? entity.getProfile().getId():null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdatedAt())
                .type(entity.getType())
                .build();

    }
}
