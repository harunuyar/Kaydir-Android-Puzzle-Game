package com.harunuyar.kaydir.States;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import com.harunuyar.kaydir.States.State;
import java.util.Stack;

/**
 * Created by Harun on 5.02.2017.
 */

public class GameStateManager {

    private Context context;
    private Activity activity;
    private Stack<State> states;

    public GameStateManager(Context context){
        this.context = context;
        states = new Stack<>();
    }

    public void pushState(State state){
        onPause();
        states.push(state);
    }

    public State peekState(){
        return states.peek();
    }

    public void popState(){
        if (!states.empty()){
            states.pop().dispose();
        }
        onResume();
    }

    public void setState(State state){
        popState();
        pushState(state);
    }

    public boolean isEmpty(){
        return states.isEmpty();
    }

    public void draw(Canvas canvas){
        if (states.empty()){
            return;
        }

        State curState = states.peek();
        curState.update();
        curState.draw(canvas);
    }

    public Context getContext(){
        return context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity(){
        return this.activity;
    }

    public void onPause(){
        if (!states.isEmpty()){
            states.peek().onPause();
        }
    }

    public void onResume(){
        if (!states.empty()){
            states.peek().onResume();
        }
    }

}
