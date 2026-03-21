package in.ankitsaahariya.WalletLedger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private String profileImageUrl;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;
    private Boolean isActive;

    @Column(name = "activation_token", nullable = true, length = 100)
    private String activationToken;

    @Column(name = "activation_token_expiry_date")
    private LocalDateTime activationTokenExpiryDate;

    @PrePersist
    public void prePersist(){
        if(this.isActive==null){
            isActive =  false;
        }
    }
}
