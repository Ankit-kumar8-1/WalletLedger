package in.ankitsaahariya.WalletLedger.controller;

import in.ankitsaahariya.WalletLedger.dto.ApiResponse;
import in.ankitsaahariya.WalletLedger.dto.ProfileDto;
import in.ankitsaahariya.WalletLedger.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

//    #1
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerProfile(@RequestBody ProfileDto request){
        ApiResponse response = profileService.registerProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/activate")
    public  ResponseEntity<ApiResponse> activationProfile(@RequestParam String token){
        profileService.activateProfile(token);
        return  ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Account activated successfully! You can now login.")
                .timeStamp(LocalDateTime.now())
                .build());
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDto authDto) {
//        try {
//
//            if (!profileService.isActive(authDto.getEmail())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
//                        "message", "Account is not active. Please activate your account first."
//                ));
//            }
//
//            Map<String, Object> response =
//                    profileServiceImpl.authenticateAndGenerateToken(authDto);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
//                    "message", e.getMessage()
//            ));
//        }
//    }

    @GetMapping("/test")
    public  String  test (){
        return "Test Succesfull";
    }

}

