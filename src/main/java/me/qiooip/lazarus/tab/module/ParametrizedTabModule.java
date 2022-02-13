package me.qiooip.lazarus.tab.module;

import lombok.Getter;
import me.qiooip.lazarus.tab.PlayerTab;

@Getter
public abstract class ParametrizedTabModule<T> extends TabModule {

    public ParametrizedTabModule(String configSection) {
        super(configSection);
    }

    public abstract void apply(PlayerTab tab, T parameter);
}
