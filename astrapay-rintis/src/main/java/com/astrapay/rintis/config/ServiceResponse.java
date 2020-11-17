package com.astrapay.rintis.config;

import com.astrapay.rintis.util.ServiceResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResponse {
    private String response_status;
    private String response_message;

    // SR Success
    public ServiceResponse successCreate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_CREATE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_CREATE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successFetch(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_FETCH.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_FETCH.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successUpdate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_UPDATE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_UPDATE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successDelete(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_DELETE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_DELETE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successMail(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_SEND_EMAIL.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_SEND_EMAIL.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successActivate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_ACTIVATE_USER.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_ACTIVATE_USER.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successLogout(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_LOGOUT.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_LOGOUT.getMessage());
        return serviceResponse;
    }

    // SR Fail
    public ServiceResponse failCreate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_CREATE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_CREATE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failFetch(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_FETCH.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_FETCH.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failUpdate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_UPDATE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_UPDATE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failDelete(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_DELETE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_DELETE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failExist(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_EXIST.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_EXIST.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failNotFound(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_NOT_FOUND.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_NOT_FOUND.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failMail(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_SEND_EMAIL.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_SEND_EMAIL.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failActivate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_ACTIVATE_USER.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_ACTIVATE_USER.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failExistNpwp(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_EXIST_NPWP.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_EXIST_NPWP.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failExistUser(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_EXIST_USER.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_EXIST_USER.getMessage());
        return serviceResponse;
    }

    public ServiceResponse oldPasswordNotSame(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_NOT_MATCH.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_NOT_MATCH.getMessage());
        return serviceResponse;
    }

    public ServiceResponse newPasswordMustDifference(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_PASSWORD_SAME.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_PASSWORD_SAME.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failIdentityNumberExist(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_IDENTITY_EXIST.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_IDENTITY_EXIST.getMessage());
        return serviceResponse;
    }

    public ServiceResponse faildataType(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_DATA_TYPE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_DATA_TYPE.getMessage());
        return serviceResponse;
    }

    public ServiceResponse mediaLinkNotFound(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_MEDIA_LINK.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_MEDIA_LINK.getMessage());
        return serviceResponse;
    }

    public ServiceResponse successGenerate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUCCESS_GENERATE_REPORT.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUCCESS_GENERATE_REPORT.getMessage());
        return serviceResponse;
    }

    public ServiceResponse failGenerate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.FAIL_GENERATE_REPORT.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.FAIL_GENERATE_REPORT.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse approved(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.APPROVED.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.APPROVED.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse declineInvalidMerchant(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_INVALID_MERCHANT.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_INVALID_MERCHANT.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse doNotHonor(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_DO_NOT_HONOR.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_DO_NOT_HONOR.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse invalidTransaction(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_INVALID_TRANSACTION.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_INVALID_TRANSACTION.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse invalidAmount(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_INVALID_AMOUNT.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_INVALID_AMOUNT.getMessage());
        return serviceResponse;
    }
    
    
    public ServiceResponse formatError(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_FORMAT_ERROR.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_FORMAT_ERROR.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse transactionNotPermittedToCardHolder(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse transactionNotPermittedToTerminal(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_TRANSACTION_NOT_PERMITTED_TO_TERMINAL.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_TRANSACTION_NOT_PERMITTED_TO_TERMINAL.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse suspectedFraud(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_SUSPECTED_FRAUD.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_SUSPECTED_FRAUD.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse suspendResponseReceivedTooLate(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.SUSPEND_RESPONSE_RECEIVED_TOO_LATE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.SUSPEND_RESPONSE_RECEIVED_TOO_LATE.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse cutOffInProcess(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_CUTOFF_IN_PROCESS.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_CUTOFF_IN_PROCESS.getMessage());
        return serviceResponse;
    }
    
    public ServiceResponse issuerOrSwitchIsInoperative(ServiceResponse serviceResponse) {
        serviceResponse.setResponse_status(ServiceResponseStatus.DECLINE_ISSUER_OR_SWITCH_IS_INOPERATIVE.getCode());
        serviceResponse.setResponse_message(ServiceResponseStatus.DECLINE_ISSUER_OR_SWITCH_IS_INOPERATIVE.getMessage());
        return serviceResponse;
    }

}
