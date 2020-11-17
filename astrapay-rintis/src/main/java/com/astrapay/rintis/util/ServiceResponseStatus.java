package com.astrapay.rintis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceResponseStatus {

    // Success Response
    SUCCESS_CREATE("CST001", "Data Successfully Created"),
    SUCCESS_FETCH("CST002", "Data Successfully Fetched"),
    SUCCESS_UPDATE("CST003", "Data Successfully Updated"),
    SUCCESS_DELETE("CST004", "Data Successfully Deleted"),
    SUCCESS_SEND_EMAIL("CST005", "Success to Send Email"),
    SUCCESS_ACTIVATE_USER("CST006", "Successfully activated user"),
    SUCCESS_LOGOUT("CST007", "User Logout Successfully"),
    SUCCESS_REFUND("CST008", "User Refund Successfully"),
    SUCCESS_INQUIRY("CST009", "User Inquiry Successfully"),


    // Failed Response
    FAIL_CREATE("CST901", "Failed to Create data"),
    FAIL_FETCH("CST902", "Failed to Fetch data"),
    FAIL_UPDATE("CST903", "Failed to Update data"),
    FAIL_DELETE("CST904", "Failed to Delete data"),
    FAIL_EXIST("CST905", "Data already exist"),
    FAIL_EXIST_USER("CST915", "User already exist"),
    FAIL_EXIST_NPWP("CST925", "Npwp already exist"),
    FAIL_NOT_FOUND("CST906", "Data not found"),
    FAIL_SEND_EMAIL("CST907", "Failed to Send Email"),
    FAIL_ACTIVATE_USER("CST006", "Fail to activate user"),
    FAIL_NOT_MATCH("CST926", "Old Password Not Match"),
    FAIL_PASSWORD_SAME("CST927", "New Password Must Differ From Old Password"),
    FAIL_IDENTITY_EXIST("USR908", "Identity Number already exist"),//jangan dihapus
    SUCCESS_GENERATE_REPORT("RPT001", "Report Successfully Generated"),
    FAIL_GENERATE_REPORT("RPT901", "Failed to Generate Report"),
    FAIL_MEDIA_LINK("RPT902", "Media Link not found"),
    FAIL_DATA_TYPE("CST928", "Data Type Not Support"),//jangan dihapus

    //Standarisasi Response Code Rintis
    APPROVED("00", "Approve"),
    DECLINE_INVALID_MERCHANT("03", "Invalid Merchant"),
    DECLINE_DO_NOT_HONOR("05", "Do not Honor"),
    DECLINE_INVALID_TRANSACTION("12", "Invalid Transaction"),
    DECLINE_INVALID_AMOUNT("13", "Invalid Amount"),
    DECLINE_INVALID_PAN("14","Invalic PAN Number"),
//    DECLINE_NO_SUCH_ISSUER("15", "No such issuer"),
    DECLINE_FORMAT_ERROR("30", "Format error"),
//    DECLINE_BANK_NOT_SUPPORTED_BY_SWITCH("31", "Bank not supported by switch"),
//    DECLINE_REQUESTED_FUNCTION_NOT_SUPPORTED("40", "Requested function not supported"),
    DECLINE_INSUFICIENT_FUNDS("51","Insuficient funds"),
    DECLINE_TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER("57", "Transaction not permitted to cardholder"),
    DECLINE_TRANSACTION_NOT_PERMITTED_TO_TERMINAL("58", "Transaction not permitted to terminal"),
    DECLINE_SUSPECTED_FRAUD("59", "Suspected fraud"),
    DECLINE_EXCEEDS_TRANSACTION_AMOUNT_LIMIT("61","Exceeds Transaction Amount Limit"),
    DECLINE_RESTRICTED_CARD("62","Restricted Card"),
    DECLINE_EXCEEDS_TRANSACTION_FREQUENCY_LIMIT("65","Exceeds Transaction Frequency Limit"),
    SUSPEND_RESPONSE_RECEIVED_TOO_LATE("68", "Suspend Transaction"),
//    DECLINE_NO_ACCOUNT("83", "No account"),
//    DECLINE_LINK_DOWN("89", "Link down"),
    DECLINE_CUTOFF_IN_PROCESS("90", "Cutoff in progress"),
    DECLINE_ISSUER_OR_SWITCH_IS_INOPERATIVE("91", "Link Down"),
    DECLINE_INVALID_ROUTING("92","Invalid Routing"),
    DECLINE_DUPLICATE_TRANSMISSION("94", "Duplicate Transmission/Duplicate QR"),
    DECLINE_SYSTEM_MALFUNCTION("96","Identical transaction has been success");
    
//    FAIL_REFUND("CST929","Fail Refund"),//jangan dihapus
//    FAIL_INQUIRY("CST930","Fail Inquiry"),//jangan dihapus

//    DECLINE_UNKNOWN_ERROR("93","Unknown error"),
//    INVALID_SIGNATURE("95", "Invalid signature"),
//    IN_PROGRESS_IDENTICAL_TRANSACTION("97","Identical transaction still on process"),
//    INSUFFICIENT_FUND("98", "Insufficient fund"),
//    REQUESTED_DATA_NOT_FOUND("99", "Requested data not found"),
//    FORCE_CREDIT("999","Force credit");

    String code;
    String message;
    }
