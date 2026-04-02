package in.ankitsaahariya.WalletLedger.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDto {
    private ProfileDto user;
    private String token;
}
