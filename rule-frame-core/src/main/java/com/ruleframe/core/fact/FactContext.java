package com.ruleframe.core.fact;

import java.util.Set;

public interface FactContext {
    
    Object getValue(String name);

    Set<String> getFactNames();

    boolean hasFact(String name);
}
