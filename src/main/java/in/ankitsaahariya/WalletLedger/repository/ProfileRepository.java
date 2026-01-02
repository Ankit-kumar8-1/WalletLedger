package in.ankitsaahariya.WalletLedger.repository;

import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // Find a profile by email (optional if not found) (Where email = ?
    Optional<ProfileEntity> findByEmail(String email);

//    Where activation_token =?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}