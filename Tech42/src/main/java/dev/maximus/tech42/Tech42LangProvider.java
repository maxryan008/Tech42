package dev.maximus.tech42;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class Tech42LangProvider extends FabricLanguageProvider {
    protected Tech42LangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        // Creative tab
        translationBuilder.add("itemGroup.tech42.tab", "Tech42");

        // Blocks
        translationBuilder.add("block.tech42.wood_gear_small", "Small Wooden Gear");
        translationBuilder.add("block.tech42.iron_gear_small", "Small Iron Gear");
        translationBuilder.add("block.tech42.iron_shaft", "Iron Shaft");
    }
}