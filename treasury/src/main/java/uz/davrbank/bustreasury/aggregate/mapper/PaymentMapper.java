package uz.davrbank.bustreasury.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uz.davrbank.bustreasury.aggregate.dto.request.PaymentRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PaymentResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Payment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentResponseDTO mfooDTO(Payment mfo);

    Payment mfoDTOToPayment(PaymentRequestDTO mfoDTO);

    void updatePaymentFromDTO(PaymentRequestDTO mfoDTO, @MappingTarget Payment mfo);
}