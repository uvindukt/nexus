package com.nexus.catalog.application.dto.web.view;


public interface View {

    // Inbound views


    public static interface Common {
    }

    public static interface Create extends Common {
    }

    public static interface Update extends Common {
    }

    // Outbound views

    public static interface Brief {
    }

    public static interface Detail extends Brief {
    }

}
