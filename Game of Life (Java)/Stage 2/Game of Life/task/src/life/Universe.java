package life;

import java.util.Arrays;

public class Universe {
    private char[][] currentState;

    public char[][] getCurrentState() {
        return currentState;
    }

    public void setCurrentState(char[][] currentState) {
        this.currentState = currentState;
    }

    public void showCurrentState(){
        Arrays.stream(this.currentState).forEachOrdered(System.out::println);
    }
}
