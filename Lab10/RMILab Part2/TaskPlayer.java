import java.io.*;
public class TaskPlayer implements Task<String>, Serializable {
    // funny task here
    private static final long serialVersionUID = 448019L;
    public TaskPlayer(){

    } 
    public String execute() {
        // play a game of russian roulette with either 1 or 2 bullets in a 6-chamber revolver and "bullet" gets the message dead and else gets safe
        return "You are playing Russian Roulette. You pull the trigger... " + (Math.random() < 0.33 ? "BANG! You're dead." : "Click! You're safe.");
    }
}
