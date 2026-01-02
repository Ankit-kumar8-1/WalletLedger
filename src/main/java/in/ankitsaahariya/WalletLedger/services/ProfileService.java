package in.ankitsaahariya.WalletLedger.services;

import in.ankitsaahariya.WalletLedger.dto.ProfileDto;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import in.ankitsaahariya.WalletLedger.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;


    public ProfileDto registerProfile(ProfileDto profileDto){
        ProfileEntity newProfile =  toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile= profileRepository.save(newProfile);
//        send activation email
        String activationLink= "http://localhost:8080/api/v1.0/activate?token=" +newProfile.getActivationToken();
        String subject = "Activate your WalletLedger account";
        String body = "Click on the following link to activate your account:" + activationLink;
        emailService.sendEmail(newProfile.getEmail(),subject,body);
        return  toDTO(newProfile);

    }

    public ProfileEntity toEntity(ProfileDto profileDto){
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName((profileDto.getFullName()))
                .email(profileDto.getEmail())
                .password(profileDto.getPassword())
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updateAt(profileDto.getUpdateAt())
                .build();
    }

    public ProfileDto toDTO(ProfileEntity profileEntity){
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName((profileEntity.getFullName()))
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updateAt(profileEntity.getUpdateAt())
                .build();
    }
}
