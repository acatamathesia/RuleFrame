package com.ruleframe.core.element;

import com.ruleframe.core.converter.ConverterRegistry;
import com.ruleframe.core.converter.ValueConverter;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.resolver.PathResolver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConfigurableElement implements Element{

    private String elName;
    private String elPath;
    private String convertName;
    private final PathResolver pathResolver;

    @Override
    public String getName() {
        return this.elName;
    }

    @Override
    public ElementValue resolve(FactContext context) {
        ElementValue ev = pathResolver.resolve(context, elPath);
        // ev在resolve方法中并没有逃逸，所以会在方法执行完成后直接垃圾回收，**栈上分配
        ValueConverter converter = ConverterRegistry.getConverter(this.convertName);
        if (converter == null) {
            return ElementValue.failure(ev.getRawValue(), this.convertName+", 缺失转换器");
        }
        Object converted = converter.convert(ev.getRawValue());
        // 获取转换后的实际类型
        Class<?> convertedType = converted != null ? converted.getClass() : null;
        return ElementValue.success(ev.getRawValue(), converted, convertedType);
    }

}