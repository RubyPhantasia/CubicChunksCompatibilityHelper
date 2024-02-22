package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

public interface IFixModule {
    public void setupModule();
    public default void preInit() {}
    public static final IFixModule NOT_ENABLED = new IFixModule() {
        @Override
        public void setupModule() {

        }
    };
}
