package com.lukaszszumiec.recurring_payments_api.application.dto;

import java.util.UUID;

public record UserPrincipal(UUID userId, String email, String role) {}
