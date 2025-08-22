import com.codingame.game.Game;
import com.codingame.game.Player;
import com.codingame.game.Agent;
import com.codingame.game.AgentClass;
import com.codingame.game.grid.Grid;
import com.codingame.game.grid.GridMaker;
import com.codingame.game.grid.Coord;
import com.codingame.game.grid.Tile;
import com.codingame.gameengine.core.AbstractPlayer;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.reflect.Field;

public class GameExporter {

    private static final int NUM_SITUATIONS = 5000; // nombre de situations à générer


    public static void main(String[] args) throws Exception {
        ArrayList<Player> players = new ArrayList<Player>(2);
        Random random;
        Grid grid;

        // long seed = ThreadLocalRandom.current().nextLong();
        long seed = -3878457299628271000L;
        random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed);
        grid = GridMaker.initGrid(random);

        String rustStruct = String.format("GameInitData {w:%d,h:%d,cells:vec![", grid.width, grid.height);
        for (int y = 0; y < grid.height; ++y) {
            for (int x = 0; x < grid.width; ++x) {
                Tile cell = grid.get(x,y);
                if (cell.getType() > 0) {
                    rustStruct += String.format("(%d,%d),", y*grid.width+x, cell.getType());
                }
            }
        }
        rustStruct +="], agents:vec![";

        Field indexField = AbstractPlayer.class.getDeclaredField("index");
        indexField.setAccessible(true);
        for (int i = 0; i < 2; i++) {
            Player player = new Player();
            players.add(player);
            // player.index = i;
            indexField.setInt(player, i);
        }

        int agentId = 1;
        List<AgentClass> agentClasses = new ArrayList<>(Arrays.asList(AgentClass.values()));
        for (int i = 0; i < 2; i++) {
            Player p = players.get(i);
            int agentIdx = 0;
            for (Coord c : grid.spawns) {
                AgentClass agentClass = agentClasses.get(agentIdx++);
                Agent a = new Agent(agentId++, agentClass);
                if (i == 1) {
                    c = grid.opposite(c);

                }
                p.agents.add(a);
                a.setOwner(p);
                a.setPosition(c);
            }

            for (Agent a : p.agents) {
                rustStruct += 
                    String.format("Agent::new(%d,%d,%d,%d,%d,%d,%d,%d)",
                            a.id,
                            a.owner.getIndex(),
                            a.maxCooldown,
                            a.getOptimalRange(),
                            a.getSoakingPower(),
                            a.getBalloons(),
                            a.getPosition().getX(),
                            a.getPosition().getY()
                        );
            }
        }
        rustStruct += "]};";
        System.out.println(rustStruct);

        System.out.println("Export terminé !");
    }

}
