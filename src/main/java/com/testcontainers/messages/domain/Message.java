package com.testcontainers.messages.domain;

import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;

public record Message(Long id, @NotEmpty String content, @NotEmpty String createdBy, Instant createdAt) {}
