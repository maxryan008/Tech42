package dev.maximus.techcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.maximus.techcore.api.pipe.PipeType;
import dev.maximus.techcore.api.pipe.TechcorePipes;
import dev.maximus.techcore.api.substance.PhaseState;
import dev.maximus.techcore.api.substance.Substance;
import dev.maximus.techcore.api.substance.SubstanceRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public class TechcoreCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("techcore")
                        .then(Commands.literal("registry")
                                .then(Commands.literal("substance")
                                        .executes(ctx -> listSubstances(ctx.getSource(), false))
                                        .then(Commands.argument("unit", StringArgumentType.word())
                                                .suggests((c, b) -> {
                                                    b.suggest("c"); return b.buildFuture();
                                                })
                                                .executes(ctx -> {
                                                    String unit = StringArgumentType.getString(ctx, "unit");
                                                    boolean celsius = unit.equalsIgnoreCase("c");
                                                    return listSubstances(ctx.getSource(), celsius);
                                                })
                                        )
                                )
                                .then(Commands.literal("pipe")
                                        .executes(ctx -> listPipes(ctx.getSource()))
                                )
                        )
        );
    }

    private static int listSubstances(CommandSourceStack source, boolean toCelsius) {
        for (Substance substance : SubstanceRegistry.getAll()) {
            String namespace = substance.getId().getNamespace();
            String name = substance.getId().getPath();

            float weight = substance.getMolecularWeight();
            float melt = convert(substance.getMeltingPoint(), toCelsius);
            float boil = convert(substance.getBoilingPoint(), toCelsius);
            float fusion = substance.getEnthalpyFusion(); // J/mol
            float vapor = substance.getEnthalpyVaporization(); // J/mol
            String unit = toCelsius ? "°C" : "K";

            source.sendSystemMessage(Component.literal(String.format(
                    "%s : %s : %.3fg/mol | Melt: %.2f%s | Boil: %.2f%s | ΔHf: %.1f J/mol | ΔHv: %.1f J/mol",
                    namespace, name, weight, melt, unit, boil, unit, fusion, vapor
            )));
        }
        return 1;
    }

    private static int listPipes(CommandSourceStack source) {
        for (var entry : TechcorePipes.getAll().entrySet()) {
            Block block = entry.getKey();
            PipeType type = entry.getValue();

            String name = block.getDescriptionId(); // Will look like "block.tech42.copper_pipe"
            float minTemp = type.getMinOperatingTemp() - 273.15f;
            float maxTemp = type.getMaxOperatingTemp() - 273.15f;
            float minPressure = type.getMinPressure();
            float maxPressure = type.getMaxPressure();

            StringBuilder phases = new StringBuilder();
            for (PhaseState phase : PhaseState.values()) {
                if (type.supportsPhase(phase)) {
                    if (phases.length() > 0) phases.append(", ");
                    phases.append(phase.name().toLowerCase());
                }
            }

            source.sendSystemMessage(Component.literal(String.format(
                    "%s | Phases: [%s] | Temp: %.1f°C–%.1f°C | Pressure: %.1f–%.1f atm",
                    name, phases, minTemp, maxTemp, minPressure, maxPressure
            )));
        }

        return 1;
    }

    private static float convert(float kelvin, boolean toCelsius) {
        return toCelsius ? kelvin - 273.15f : kelvin;
    }
}