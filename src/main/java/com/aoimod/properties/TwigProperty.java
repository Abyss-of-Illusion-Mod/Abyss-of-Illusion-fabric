package com.aoimod.properties;

import com.aoimod.blocks.Twig;
import net.minecraft.state.property.Property;

import java.util.List;
import java.util.Optional;

public class TwigProperty extends Property<Twig.TwigTypeEnum> {
    private final List<Twig.TwigTypeEnum> values;
    public TwigProperty(String name, Class<Twig.TwigTypeEnum> type) {
        super(name, type);
        values = Twig.TwigTypeEnum.record.values().stream().toList();
    }

    @Override
    public List<Twig.TwigTypeEnum> getValues() {
        return values;
    }

    @Override
    public String name(Twig.TwigTypeEnum value) {
        return value.toString();
    }

    @Override
    public Optional<Twig.TwigTypeEnum> parse(String name) {
        return Optional.ofNullable(Twig.TwigTypeEnum.record.get(name));
    }

    @Override
    public int ordinal(Twig.TwigTypeEnum value) {
        return values.contains(value) ? values.indexOf(value) : -1;
    }
}