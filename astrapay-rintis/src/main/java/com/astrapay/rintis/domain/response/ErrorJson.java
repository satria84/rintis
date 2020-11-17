package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorJson {
    private String code;
    private String type;
    private String text;
}
