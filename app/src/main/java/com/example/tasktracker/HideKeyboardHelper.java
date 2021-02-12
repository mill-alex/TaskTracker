package com.example.tasktracker;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/* Helper class for static methods that help with hiding the soft keyboard.
   Currently, this class in only used to help the TextEdit UI feature, which
   brings up the soft keyboard but doesn't hide it.
*/
public class HideKeyboardHelper {
    //Hide the soft keyboard from the screen
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Listener which can be attached to any view. If attached, it will automatically hide the keyboard when
    //the view loses focus
    public static View.OnFocusChangeListener hideKeyboardOnUnfocus()
    {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    HideKeyboardHelper.hideKeyboard(v);
                }
            }
        };
    }
}
