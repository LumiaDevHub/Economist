package io.lumia.lumiadevhub.API;

public final class EconomistProvider {
    private static EconomistAPI api;

    private EconomistProvider() {}

    public static void register(EconomistAPI impl) {
        if (api != null)
            throw new IllegalStateException("✅ | EconomistAPI уже зарегистрирован!");
        api = impl;
    }

    public static EconomistAPI getApi() {
        if (api == null)
            throw new IllegalStateException("❌ | Ошибка: EconomistAPI не зарегистрирован!");
        return api;
    }
}
