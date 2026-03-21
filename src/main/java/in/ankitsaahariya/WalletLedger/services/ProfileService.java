package in.ankitsaahariya.WalletLedger.services;


import in.ankitsaahariya.WalletLedger.dto.ApiResponse;
import in.ankitsaahariya.WalletLedger.dto.ProfileDto;

public interface ProfileService {

    public ApiResponse registerProfile(ProfileDto request);

    public  void activateProfile(String activationToken);
}
