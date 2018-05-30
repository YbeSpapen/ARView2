package com.ybe.arview2;

import com.google.ar.sceneform.math.Vector3;
import com.ybe.arview2.models.Animation;
import com.ybe.arview2.models.Sign;
import com.ybe.arview2.models.SignText;

import java.util.ArrayList;

public class DefaultConfig {
    public static Animation getDefaultAnimation() {
        Vector3 balloonPosition = new Vector3(2f, 1.9f, 0.0f);
        Vector3 balloonScale = new Vector3(0.5f, 0.5f, 0.5f);
        return new Animation("balloon.sfb", 8000, balloonPosition, balloonScale);
    }

    public static Sign getDefaultSignSmall() {
        return new Sign("Wegwijzer", "signobj.sfb", "sign", new Vector3(0.40f, 0.40f, 0.40f));
    }

    public static Sign getDefaultSignBig() {
        return new Sign("Wegwijzer groot", "signobj.sfb", "sign", new Vector3(0.60f, 0.60f, 0.60f));
    }

    public static ArrayList<Sign> getDefaultSignList() {
        ArrayList<Sign> signs = new ArrayList<>();
        signs.add(new Sign("Wegwijzer", "signobj.sfb", "sign", new Vector3(0.40f, 0.40f, 0.40f)));
        signs.add(new Sign("Wegwijzer groot", "signobj.sfb", "sign", new Vector3(0.60f, 0.60f, 0.60f)));
        return signs;
    }

    public static SignText getDefaultText() {
        Vector3 textPosition = new Vector3(0f, 0.62f, 0.03f);
        Vector3 textPositionReflected = new Vector3(0f, 0.62f, -0.03f);
        Vector3 textScale = new Vector3(0.5f, 0.5f, 0.5f);
        SignText text = new SignText(textPosition, textScale);
        text.setPositionReflected(textPositionReflected);
        return text;
    }
}
