package in.ankitsaahariya.WalletLedger.controller;

import in.ankitsaahariya.WalletLedger.services.DashBoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final DashBoardService dashBoardService;


    @GetMapping
    private ResponseEntity<Map<String,Object>> getDashboardData(){
        Map<String,Object> dashboardData = dashBoardService.getDashboardData();
        return  ResponseEntity.ok(dashboardData);
    }
}
