package com.nexus.catalog.application.dto.web;

import jakarta.validation.groups.Default;

/**
 * Any constraint without a group automatically belongs to {@link Default}
 * {@link Default} is the shared group
 */
public interface Validate {

    public static interface Create extends Default {
    }

    public static interface Update extends Default {
    }

}
