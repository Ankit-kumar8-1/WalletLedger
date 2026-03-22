package in.ankitsaahariya.WalletLedger.servicesImp;

import in.ankitsaahariya.WalletLedger.dto.ApiResponse;
import in.ankitsaahariya.WalletLedger.dto.AuthDto;
import in.ankitsaahariya.WalletLedger.dto.LoginRequest;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
    public ApiResponse registerProfile(ProfileDto request){
        if (profileRepository.existsByEmail(request.getEmail())){
            throw   new UserAlreadyExistsException("User Already Exists !");
        }
        ProfileEntity newProfile =  ProfileEntity.builder()
                .id(request.getId())
                .fullName((request.getFullName()))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .createdAt(request.getCreatedAt())
                .updateAt(request.getUpdateAt())
                .build();

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

    @Override
    public AuthDto login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                throw new RuntimeException("Authentication failed");
            }

        } catch (DisabledException ex) {
            throw new RuntimeException("Account is disabled. Please verify your email.");
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid email or password.");
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred during login. Please try again later.");
        }

        String token = jwtUtil.generateToken(request.getEmail());

        return AuthDto.builder()
                .token(token)
                .user(getPublicProfile(request.getEmail()))
                .build();
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

    private String generateSecureActivationToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return  Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }




}
