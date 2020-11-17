package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogonRequest {
    public String message_type;
    public String primary_bit_map;
    public String secondary_bit_map;
    public String transmission_date_and_time;
    public String system_trace_audit_number;
    public String additional_data;
    public String network_management_information_code;
    public String message;
}
