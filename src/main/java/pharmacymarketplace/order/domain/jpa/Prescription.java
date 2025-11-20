package pharmacymarketplace.order.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "prescription_number", unique = true)
    private String prescriptionNumber;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_crm")
    private String doctorCrm;

    @Column(name = "prescription_date")
    private Instant prescriptionDate;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.PENDING;

    public enum PrescriptionStatus {
        PENDING,
        APPROVED,
        REJECTED,
        EXPIRED
    }
}

