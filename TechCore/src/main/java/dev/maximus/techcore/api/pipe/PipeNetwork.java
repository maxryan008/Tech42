package dev.maximus.techcore.api.pipe;

import dev.maximus.techcore.api.substance.PhaseState;
import dev.maximus.techcore.api.substance.Substance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.*;

public class PipeNetwork {
    private final Set<BlockPos> pipePositions = new HashSet<>();
    private final Map<BlockPos, PipeState> states = new HashMap<>();

    public void addPipe(BlockPos pos, PipeState state) {
        pipePositions.add(pos);
        states.put(pos, state);
    }

    public void tick(Level level) {
        // Example simulation tick:
        for (BlockPos pos : pipePositions) {
            PipeState state = states.get(pos);
            simulateGravity(pos, state);
            equalizeWithNeighbors(pos, state, level);
            handleSolidification(pos, state, level);
        }
    }

    private void simulateGravity(BlockPos pos, PipeState state) {
        // Approximate weight per mol = Σ(molWeight * %ratio)
        float densityFactor = 0.0f;
        for (var entry : state.getContents().entrySet()) {
            densityFactor += entry.getKey().getMolecularWeight() * entry.getValue();
        }
        // Apply pressure from gravity (simplified)
        float gravityPressure = 0.01f * densityFactor;
        state.setPressure(state.getPressure() + gravityPressure);
    }

    private void equalizeWithNeighbors(BlockPos pos, PipeState state, Level level) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            PipeState neighborState = states.get(neighbor);
            if (neighborState != null) {
                float delta = (state.getPressure() - neighborState.getPressure()) / 2f;
                state.setPressure(state.getPressure() - delta);
                neighborState.setPressure(neighborState.getPressure() + delta);
            }
        }
    }

    private void handleSolidification(BlockPos pos, PipeState state, Level level) {
        for (Substance substance : new ArrayList<>(state.getContents().keySet())) {
            PhaseState phase = substance.getPhase(
                    state.getTemperature() - 273.15f,
                    state.getPressure()
            );
            if (phase == PhaseState.SOLID && !TechcorePipes.getPipe(level.getBlockState(pos).getBlock()).supportsPhase(phase)) {
                TechcorePipes.getPipe(level.getBlockState(pos).getBlock())
                        .onPhaseIncompatibility(new PipeContext(this, pos, state), phase);
            }
        }
    }
}