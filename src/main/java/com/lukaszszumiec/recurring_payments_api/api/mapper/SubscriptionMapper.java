package com.lukaszszumiec.recurring_payments_api.api.mapper;

import com.lukaszszumiec.recurring_payments_api.domain.model.Subscription;
import com.lukaszszumiec.recurring_payments_api.generated.model.SubscriptionDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "price", expression = "java(s.getPrice().toPlainString())")
    SubscriptionDto toDto(Subscription s);
}
