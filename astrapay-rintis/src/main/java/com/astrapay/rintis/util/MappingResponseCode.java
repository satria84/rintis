package com.astrapay.rintis.util;

public class MappingResponseCode {
    private MappingResponseCode(){

    }
    public static String getResponseText(String responseCode){
        String responseText;
        switch (responseCode){
            case "00":
                responseText="Approved";
                break;
            case "03":
                responseText="Invalid Merchant, this RC can be used by acquirer if there is no longer business with the merchant";
                break;
            case "05":
                responseText="Do not Honor";
                break;
            case "12":
                responseText="Invalid Transaction";
                break;
            case "13":
                responseText="Invalid Amount";
                break;
            case "14":
                responseText="Invalid PAN Number";
                break;
            case "30":
                responseText="Format Error";
                break;
            case "51":
                responseText="Insuficient funds";
                break;
            case "57":
                responseText="Transaction Not Permitted to Cardholder / QR is expired";
                break;
            case "58":
                responseText="Transaction Not Permitted to Terminal";
                break;
            case "59":
                responseText="Suspected Fraud";
                break;
            case "61":
                responseText="Exceeds Transaction Amount Limit";
                break;
            case "62":
                responseText="Restricted Card";
                break;
            case "65":
                responseText="Exceeds Transaction Frequency Limit";
                break;
            case "68":
                responseText="Suspend transaction";
                break;
            case "90":
                responseText="Cut off in progress";
                break;
            case "91":
                responseText="Link down";
                break;
            case "92":
                responseText="Invalid Routing";
                break;
            case "94":
                responseText="Duplicate Transmission / Duplicate QR";
                break;
            case "96":
                responseText="System Malfunction";
                break;
            case "A0":
                responseText="QR Payment Fail (Mutation not perform)";
                break;
            case "Timeout":
                responseText="Timeout";
                break;
            default:
                responseText="Unknown Response Code";
                break;
        }
        return  responseText;
    }
}
