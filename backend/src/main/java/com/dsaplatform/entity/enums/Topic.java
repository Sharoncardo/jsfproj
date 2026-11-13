package com.dsaplatform.entity.enums;

public enum Topic {
    ARRAYS("Arrays"),
    STRINGS("Strings"),
    LINKED_LIST("Linked List"),
    STACK_QUEUE("Stack & Queue"),
    TREES("Trees & BST"),
    GRAPHS("Graphs"),
    DYNAMIC_PROGRAMMING("Dynamic Programming"),
    SORTING_SEARCHING("Sorting & Searching"),
    GREEDY("Greedy Algorithms"),
    BACKTRACKING("Backtracking");

    private final String displayName;

    Topic(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
