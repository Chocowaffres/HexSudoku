package hexsudoku;


public class Game_Stats {
    
    private String difficulty;
    private String tempo;

    public Game_Stats(String difficulty, String tempo) {
        this.difficulty = difficulty;
        this.tempo = tempo;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
    
}
