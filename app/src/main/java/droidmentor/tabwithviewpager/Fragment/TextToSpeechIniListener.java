package droidmentor.tabwithviewpager.Fragment;

import android.speech.tts.TextToSpeech;

public interface TextToSpeechIniListener {
    public void onSucces(TextToSpeech tts);

    public void onFailure(TextToSpeech tts);
}
