package com.nexus.inventory.application.dto.web;

import jakarta.validation.groups.Default;

public interface Validate {

    public static interface Create extends Default {
    }

    public static interface Update extends Default {
    }

}
