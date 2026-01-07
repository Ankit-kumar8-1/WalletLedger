package in.ankitsaahariya.WalletLedger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WalletLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletLedgerApplication.class, args);
        System.out.println("Application started .......");
	}

}
