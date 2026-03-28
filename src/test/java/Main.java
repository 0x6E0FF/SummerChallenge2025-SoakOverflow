import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;

public class Main {
    public static void main(String[] args) {

        int LEAGUE = 5;

        for (int i = 0; i < 1000; i++) {
            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
            gameRunner.setLeagueLevel(LEAGUE);

            // Set seed here (leave commented for random)
            gameRunner.setSeed(2842837289064092126L);

            // Select agents here
            gameRunner.addAgent("../soak/target/release/soak", "Player 1");
            gameRunner.addAgent("../soak/target/release/soak", "Player 2");

            // gameRunner.start(8888);


            GameResult res = gameRunner.simulate();
            int myScore = res.scores.get(0);
            int oppScore = res.scores.get(1);

            System.out.println(myScore + " / " + oppScore);
        }
    }
}
