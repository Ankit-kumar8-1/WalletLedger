package in.ankitsaahariya.WalletLedger.controller;

import in.ankitsaahariya.WalletLedger.dto.ApiResponse;
import in.ankitsaahariya.WalletLedger.dto.AuthDto;
import in.ankitsaahariya.WalletLedger.dto.LoginRequest;
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

    @PostMapping("/login")
    public  ResponseEntity<AuthDto> login(@RequestBody LoginRequest request){
        AuthDto response = profileService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public  String  test (){
        return "Test Succesfull";
    }

}

