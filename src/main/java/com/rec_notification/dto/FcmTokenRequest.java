package com.rec_notification.dto;

import com.rec_notification.enums.DeviceType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmTokenRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Token is required")
    private String token;

    @NotNull(message = "Device type is required")
    private DeviceType deviceType;
}