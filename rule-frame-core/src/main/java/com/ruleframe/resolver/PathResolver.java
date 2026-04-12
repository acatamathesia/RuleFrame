package com.ruleframe.resolver;

import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;

public interface PathResolver {
    
    ElementValue resolve(FactContext context, String path);

}
