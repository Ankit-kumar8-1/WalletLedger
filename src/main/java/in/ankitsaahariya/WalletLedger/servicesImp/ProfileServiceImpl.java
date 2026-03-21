package in.ankitsaahariya.WalletLedger.servicesImp;

import in.ankitsaahariya.WalletLedger.dto.ApiResponse;
import in.ankitsaahariya.WalletLedger.dto.AuthDto;
import in.ankitsaahariya.WalletLedger.dto.ProfileDto;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import in.ankitsaahariya.WalletLedger.exceptions.InvalidTokenException;
import in.ankitsaahariya.WalletLedger.exceptions.TokenExpiredException;
import in.ankitsaahariya.WalletLedger.exceptions.UserAlreadyExistsException;
import in.ankitsaahariya.WalletLedger.repository.ProfileRepository;
import in.ankitsaahariya.WalletLedger.security.JwtUtil;
import in.ankitsaahariya.WalletLedger.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse registerProfile(ProfileDto profileDto){
        if (profileRepository.existsByEmail(profileDto.getEmail())){
            throw   new UserAlreadyExistsException("User Already Exists !");
        }
        ProfileEntity newProfile =  toEntity(profileDto);
        String token = generateSecureActivationToken();
        newProfile.setActivationToken(token);
        newProfile.setActivationTokenExpiryDate(LocalDateTime.now().plusHours(24));
        profileRepository.save(newProfile);
        emailService.sendActivationEmail(
                newProfile.getEmail(),
                newProfile.getActivationToken(),
                newProfile.getFullName());

        return  ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .timeStamp(LocalDateTime.now())
                .message("Registration successful! Please check your email to activate your account.")
                .build();

    }

    private String generateSecureActivationToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return  Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public ProfileEntity toEntity(ProfileDto request){
        return ProfileEntity.builder()
                .id(request.getId())
                .fullName((request.getFullName()))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .createdAt(request.getCreatedAt())
                .updateAt(request.getUpdateAt())
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


    @Override
    public  void  activateProfile(String activationToken){
        ProfileEntity profile = profileRepository.findByActivationToken(activationToken)
                .orElseThrow(()-> new InvalidTokenException("Invalid or already used activation link"));

        if (profile.getActivationTokenExpiryDate().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Activation link has expired. Please register again.");
        }

        profile.setIsActive(true);
        profile.setActivationToken(null);
        profile.setActivationTokenExpiryDate(null);
        profileRepository.save(profile);
    }

//    3
    public  boolean isActive(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public  ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  profileRepository.findByEmail(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("Profile not found with email :" + authentication.getName()));
    }

    public  ProfileDto getPublicProfile(String email){
        ProfileEntity currentUser ;
        if(email == null){
            currentUser = getCurrentProfile();
        }else {
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("Profile not found with email:"+ email));
        }

        return ProfileDto.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updateAt(currentUser.getUpdateAt())
                .build();
    }

    public Map<String,Object> authenticateAndGenerateToken(AuthDto authDto){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),authDto.getPassword()));
//            Generate jwt token
            String token  = jwtUtil.generateToken(authDto.getEmail());
            return Map.of(
                    "token",token,
                    "user",getPublicProfile(authDto.getEmail())
            );
        }catch (Exception e){
            throw  new RuntimeException("Invalid email or Password");
        }
    }
}
