package pharmacymarketplace.order.dtos;

import pharmacymarketplace.order.domain.jpa.Prescription.PrescriptionStatus;

import java.time.Instant;

public record PrescriptionDto(
        Long id,
        String prescriptionNumber,
        String doctorName,
        String doctorCrm,
        Instant prescriptionDate,
        String fileUrl,
        String notes,
        PrescriptionStatus status
) {}

