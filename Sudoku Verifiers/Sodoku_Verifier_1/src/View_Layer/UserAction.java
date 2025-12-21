package View_Layer;

import java.io.IOException;

public class UserAction { // iued in logging

    private ViewFacade viewFacade;
    private String action;

    public static final String START_GAME = "start";
    public static final String END_GAME = "end";
    // string for adding a value for an incomplete elemnent = "x,y,val,prev"
    public static final String UNDO = "undo";

    public UserAction() throws IOException {
        viewFacade = new ViewFacade();
        viewFacade.logUserAction(this.start());
    }

    private UserAction start() {
        action = START_GAME;
        return this;
    }

    public UserAction end() {
        action = END_GAME;
        return this;
    }

    public UserAction addValue(int x, int y, int val, int prev) {
        action = x + "," + y + "," + val + "," + prev;
        return this;
    }

    @Override
    public String toString() {
        return action;
    }

}
