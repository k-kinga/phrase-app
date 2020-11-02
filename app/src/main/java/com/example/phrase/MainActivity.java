package com.example.phrase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button copy;
    private Button search;
    private Button reset;
    private String pasteData;
    private TextView found;
    private String phrase;
    private String initialText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialText = "Paste text from clipboard...";
        textView = (TextView) findViewById(R.id.textView);
        found = (TextView) findViewById(R.id.numberOfFound);
        copy = (Button) findViewById(R.id.topButton);
        search = (Button) findViewById(R.id.findButton);
        reset = (Button) findViewById(R.id.resetButton);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                pasteData = "";
                if (!(clipboard.hasPrimaryClip())) {

                    textView.setEnabled(false);

                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))) {

                    // This disables the paste menu item, since the clipboard has data but it is not plain text
                    textView.setEnabled(false);
                } else {

                    // This enables the paste menu item, since the clipboard contains plain text.
                    textView.setEnabled(true);
                }

                ClipData.Item toPaste = clipboard.getPrimaryClip().getItemAt(0);
                pasteData = (String) toPaste.getText();
                textView.setText(pasteData);
                initialText = pasteData;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View view) {
                                          AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                          LayoutInflater inflater = (LayoutInflater) MainActivity.this.getLayoutInflater();
                                          View v = inflater.inflate(R.layout.popup_layout, null);
                                          builder.setView(v);
                                          final EditText searchedFor = (EditText) v.findViewById(R.id.searchedText);


                                          builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                              @SuppressLint("SetTextI18n")
                                              public void onClick(DialogInterface dialog, int id) {
                                                  phrase = searchedFor.getText().toString();
                                                  ArrayList<Integer> list = searchForInstancesOfText();
                                                  found.setText("found:" + list.size());

                                              }
                                          });
                                          builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int id) {
                                                  dialog.cancel();
                                              }
                                          });
                                          builder.show();


                                      }
                                  }


        );

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                found.setText("");

            }
        });

    }

    private ArrayList<Integer> searchForInstancesOfText() {

        ArrayList<Integer> indexes = new ArrayList<>();
        SpannableString s = new SpannableString(initialText);
        String lowerCaseWord = phrase.toLowerCase();

        int index = 0;
        while (index != -1) {
            index = s.toString().indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                s.setSpan(new BackgroundColorSpan(Color.YELLOW), index, index + phrase.length(), 0);
                index++;
            }
        }


        textView.setText(s);
        return indexes;
    }


}