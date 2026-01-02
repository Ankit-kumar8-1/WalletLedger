package in.ankitsaahariya.WalletLedger.controller;

import in.ankitsaahariya.WalletLedger.dto.ProfileDto;
import in.ankitsaahariya.WalletLedger.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

//    #1
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto){
        ProfileDto registerProfile =  profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

//    #2
    @GetMapping("/activate")
    public  ResponseEntity<String> activationProfile(@RequestParam String token){
        boolean isActivated =  profileService.activateProfile(token);
        if(isActivated){
            return ResponseEntity.ok("Profile activated successfuly !");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actiation token not found or already used");
        }
    }

}
