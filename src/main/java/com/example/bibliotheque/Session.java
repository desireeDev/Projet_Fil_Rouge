package com.example.bibliotheque;

public class Session {
    private static String currentUserRole;

    public static void setCurrentUserRole(String role) {
        currentUserRole = role;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(currentUserRole);
    }
}
