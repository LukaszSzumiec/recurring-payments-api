package com.lukaszszumiec.recurring_payments_api.api.mapper;

import com.lukaszszumiec.recurring_payments_api.domain.model.Payment;
import com.lukaszszumiec.recurring_payments_api.generated.model.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring", imports = { ZoneOffset.class })
public interface PaymentMapper {

    @Mapping(target = "subscriptionId", source = "subscription.id")
    @Mapping(target = "amount", expression = "java(entity.getAmount().toPlainString())")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().atOffset(ZoneOffset.UTC))")
    PaymentDto toDto(Payment entity);
}
