package com.br.digitalmoney.accountservice.activity.assembler;

import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.dto.response.ActivityResponse;
import com.br.digitalmoney.accountservice.activity.entities.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    @Mapping(source = "transactionId", target = "transaction.id")
    @Mapping(source = "accountId", target = "account.id")
    ActivityEntity fromActivityToActivityEntity(Activity activity);
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "account.id", target = "accountId")
    Activity fromActivityEntityToActivity(ActivityEntity activityEntity);
    ActivityResponse fromActivityToActivityResponse(Activity activity);
}
