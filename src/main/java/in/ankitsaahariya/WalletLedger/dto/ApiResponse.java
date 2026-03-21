package in.ankitsaahariya.WalletLedger.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
