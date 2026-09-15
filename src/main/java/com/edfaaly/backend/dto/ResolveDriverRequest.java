package com.edfaaly.backend.dto;

import lombok.Data;

@Data
public class ResolveDriverRequest {
    /** قيمة الـ QR إذا كان الراكب مسح كود */
    private String qrValue;

    /** كود السائق القصير أو رقم اللوحة إذا كان الراكب أدخل يدويًا */
    private String manualCode;
}
