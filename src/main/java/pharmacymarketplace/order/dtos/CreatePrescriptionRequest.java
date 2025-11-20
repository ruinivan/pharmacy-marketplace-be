package pharmacymarketplace.order.dtos;

public record CreatePrescriptionRequest(
        String prescriptionNumber,
        String doctorName,
        String doctorCrm,
        String fileUrl,
        String notes
) {}

