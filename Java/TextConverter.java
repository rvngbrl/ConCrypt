package com.example.rvn_gbrl.navigationsample;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class TextConverter extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;
    TextView textView, NameCnvsn, textView1;
    TextView binarytv;
    TextView binarysimul;
    Button copyText, simul, BtnOk, pasteText;

    String screenshot;
    Spinner spinner;
    EditText inputtext, display_data, displaySimulation, txtAnswr;
    String choose[] = {"Text to Binary", "Binary to Text", "Decimal to Binary", "Binary to Decimal", "Decimal to Hexadecimal", "Hexadecimal to Decimal"};
    ArrayAdapter<String> adapter;

    String record = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }
        setContentView(R.layout.activity_text_converter);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        spinner = (Spinner) findViewById(R.id.spinner3);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, choose);

        display_data = (EditText) findViewById(R.id.outputText);
        spinner.setAdapter(adapter);
        display_data.setKeyListener(null);
        inputtext = (EditText) findViewById(R.id.inputText);

        final TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
        final TextView binarysimul = (TextView) findViewById(R.id.outputTextView);

        simul = (Button) findViewById(R.id.txtbtn);
        simul.setEnabled(false);
        simul = (Button) findViewById(R.id.txtbtn);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Binary to Decimal")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    KeyListener keyListener = DigitsKeyListener.getInstance("01");
                    inputtext.setKeyListener(keyListener);
                } else if (spinner.getSelectedItem().toString().equals("Decimal to Binary")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setSingleLine(false);
                    inputtext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    KeyListener keyListener = DigitsKeyListener.getInstance("0123456789");
                    inputtext.setKeyListener(keyListener);
                } else if (spinner.getSelectedItem().toString().equals("Text to Binary")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setSingleLine(false);
                    inputtext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                } else if (spinner.getSelectedItem().toString().equals("Binary to Text")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setSingleLine(false);
                    inputtext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    KeyListener keyListener = DigitsKeyListener.getInstance("01 ");
                    inputtext.setKeyListener(keyListener);
                } else if (spinner.getSelectedItem().toString().equals("Decimal to Hexadecimal")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setSingleLine(false);
                    inputtext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    KeyListener keyListener = DigitsKeyListener.getInstance("0123456789");
                    inputtext.setKeyListener(keyListener);
                } else if (spinner.getSelectedItem().toString().equals("Hexadecimal to Decimal")) {
                    display_data.getText().clear();
                    inputtext.getText().clear();
                    inputtext.setSingleLine(false);
                    inputtext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setOnItemSelectedListener(this);
                switch (position) {

                    case 0: //ali abe
                        display_data.getText().clear();
                        spinner.setOnItemSelectedListener(this);


                        EditText inputtext = (EditText) findViewById(R.id.inputText);
                        String tta = inputtext.getText().toString();
                        StringBuilder asciiatt = new StringBuilder();

                        // String charArray = att.toString();
                        int[] res = new int[tta.length()];
                        for (int i = 0; i < tta.length(); i++) {
                            asciiatt.append(res[i] = tta.charAt(i)).toString();
                            asciiatt.append(" ");

                        }

                        record = asciiatt.toString();

                        display_data.setTextSize(18);
                        display_data.setText(record);



                        break;
                    case 1: //ali abe
                        display_data.getText().clear();
                        try {
                            spinner.setOnItemSelectedListener(this);
                            EditText inputext = (EditText) findViewById(R.id.inputText);
                            StringBuilder asciitta = new StringBuilder();
                            String att = inputext.getText().toString();

                            String[] charascii = att.split("[^0-9]+");
                            for (String parseInt : charascii) {

                                asciitta.append((char) Integer.parseInt(parseInt));
                            }
                            record = asciitta.append(" ").toString();
                        } catch (Exception ex) {

                            record = "input text";
                        }

                        display_data.setTextSize(18);
                        display_data.setText(record);


                        break;


                    case 2: //text to binary
                        display_data.getText().clear();
                        spinner.setOnItemSelectedListener(this);
                        inputtext = (EditText) findViewById(R.id.inputText);
                        String ttb = inputtext.getText().toString();

                        byte[] bytesttb = ttb.getBytes();


                        StringBuilder binaryttb = new StringBuilder();
                        for (byte b : bytesttb) {
                            int val = b;
                            for (int i = 0; i < 8; i++) {

                                binaryttb.append((val & 128) == 0 ? 0 : 1);
                                val <<= 1;
                            }
                            record = binaryttb.append(" ").toString();
                        }
                        display_data.getText().clear();
                        display_data.setTextSize(18);
                        display_data.setText(record);
                        simul.setEnabled(true);

                        break;

                    case 3:
                        display_data.getText().clear();

                        inputtext = (EditText) findViewById(R.id.inputText);
                        String btt = inputtext.getText().toString().replace(" ", "");
                        if (TextUtils.isDigitsOnly(btt)) {
                            StringBuilder binarybtt = new StringBuilder();

                            for (int i = 0; i <= btt.length() - 8; i += 8) {
                                //  binarybtt= btt + ((char) Integer.parseInt(btt.substring(i, i + 8), 2));


                                binarybtt.append((char) Integer.parseInt(btt.substring(i, i + 8), 2));
                            }


                            record = binarybtt.append(" ").toString();
                        }

                        display_data.setTextSize(18);
                        display_data.setText(record);
                        simul.setEnabled(true);


                        break;


                    case 4: //binary to decimal ni luz
                        display_data.getText().clear();
                        try {


                            spinner.setOnItemSelectedListener(this);
                            inputtext = (EditText) findViewById(R.id.inputText);
                            Double n = Double.parseDouble(inputtext.getText().toString());

                            StringBuilder binarytodec = new StringBuilder();

                            double s = n;
                            int c = 0;
                            long s1 = 0;
                            while (s > 0) {
                                s1 = s1 + (long) (Math.pow(2, c) * (long) (s % 10));
                                s = (long) s / 10;
                                c++;
                            }
                            record = binarytodec.append(s1).toString();

                            display_data.setTextSize(18);
                            display_data.setText(record);


                        } catch (Exception ex) {

                            record = "input text";
                        }
                        break;

                    case 5: //decimal to binary

                        display_data.append("Decimal to Binary");
                        inputtext = (EditText) findViewById(R.id.inputText);

                        String decimalnum = inputtext.getText().toString(); //inputted text
                        StringBuilder dectobinary = new StringBuilder();
                        Float num1 = Float.parseFloat(decimalnum);
                        Float test = num1 % 1;
                        long r;
                        Long num ;

                        display_data.getText().clear();
                        binarytv.setText("");
                        num = Long.parseLong(decimalnum);


                        try {
                            while (num != 0) {
                               binarysimul.append(num + "/ 2 = "); //bibring down
                                r = num % 2;
                                num = num / 2;
                               binarysimul.append(num + "  R  " + r + "\n");  //eto yung ishoshow na simulation
                                binarytv.append(r + ""); //eto yung nakabaligtad
                            }
                           // display_data.getText().clear();

                            String bin = String.valueOf(binarytv.getText()); //eto yung babaligtarin

                            record = dectobinary.append(new StringBuffer(bin).reverse()).toString();




                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                        break;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
*/


        textView = (TextView) findViewById(R.id.outputText);
        textView1 = (TextView) findViewById(R.id.inputText);
        pasteText = (Button) findViewById(R.id.editpaste);
        copyText = (Button) findViewById(R.id.bCopy);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager Clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        copyText.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                String text = textView.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
        pasteText.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipData cd2 = myClipboard.getPrimaryClip();
                ClipData.Item item = cd2.getItemAt(0);
                String copied = item.getText().toString();
                textView1.setText(copied);
            }
        });


        simul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mEBuilder = new AlertDialog.Builder(TextConverter.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_simulation, null);
                NameCnvsn = (TextView) mView.findViewById(R.id.cnvrsnName);
                txtAnswr = (EditText) mView.findViewById(R.id.AnswerTxt);
                txtAnswr.setKeyListener(null);


                displaySimulation = (EditText) mView.findViewById(R.id.outputSimulation);
                displaySimulation.setKeyListener(null);
                EditText inputtext = (EditText) findViewById(R.id.inputText);


                String getText = inputtext.getText().toString();
                String gettextoutput = display_data.getText().toString();


                txtAnswr.setText(getText + " = " + gettextoutput);
                NameCnvsn.setText(spinner.getSelectedItem().toString());


                String simulation = binarysimul.getText().toString();
                String presimul = binarytv.getText().toString();

                mEBuilder.setView(mView);
                final AlertDialog dialog = mEBuilder.create();

                Button button = (Button)findViewById(R.id.txtbtn);
                final Animation myAnim = AnimationUtils.loadAnimation(TextConverter.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                button.startAnimation(myAnim);

                spinner.getSelectedItem();
                if (spinner.getSelectedItem().equals("Binary to Decimal")) {
                    //displaySimulation.setText(simulation+"\n\n"+presimul);
                    displaySimulation.setText("");
                    displaySimulation.setText("Multiplying each digit of " + getText + " into 2 raised to the number of digits minus 1 and repeating the process while decrementing thr power by 1 until it becomes 0, " + simulation + "\n" + "Then adding up all the product of each, the decimal equivalent of " + getText + " is " + gettextoutput);
                } else if (spinner.getSelectedItem().equals("Decimal to Binary")) {
                    displaySimulation.setText("");
                    displaySimulation.setText("Dividing " + getText + " by 2, getting the reminder, then using the qoutient as the new dividend, until the qoutient ends up to to 0, \n" + simulation + "\nThen getting the remainder (R) from bottom to top, the binary equivalent of " + getText + " is " + gettextoutput);


                } else if (spinner.getSelectedItem().equals("Text to Binary")) {
                    displaySimulation.setText("");
                    displaySimulation.setText("First is to get the decimal equivalent of each character based on Decimal ASCII Chart.\n\n" + simulation + "\nSummarizing all the binary from top to bottom, the binary equivalent of \"" + getText + "\" is: " + gettextoutput);
                }
                else if (spinner.getSelectedItem().equals("Binary to Text")) {
                    displaySimulation.setText("");
                    displaySimulation.setText(simulation+"\n");
                }
                else if (spinner.getSelectedItem().equals("Decimal to Hexadecimal")) {
                    displaySimulation.setText("");
                    displaySimulation.setText(simulation+gettextoutput);
                }
                else if (spinner.getSelectedItem().equals("Hexadecimal to Decimal")) {
                    displaySimulation.setText("");
                    displaySimulation.setText(simulation);
                }
                Button copyText1 = (Button) mView.findViewById(R.id.bCopy1);
                copyText1.setOnClickListener(new View.OnClickListener()

                {
                    public void onClick(View v) {
                        // TODO Auto-generated method stub


                        String text = displaySimulation.getText().toString();
                        myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(getApplicationContext(), "Text Copied",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Button mProceed = (Button) mView.findViewById(R.id.okbtn);
                mProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }


    public void Del(View v) {
        EditText inputtext = (EditText) findViewById(R.id.inputText);
        inputtext.getText().clear();
        display_data.getText().clear();
    }

    public void Delete(View v) {
        EditText inputtext = (EditText) findViewById(R.id.inputText);
        inputtext.getText().clear();
        EditText outputext = (EditText) findViewById(R.id.outputText);
        outputext.getText().clear();
        display_data.getText().clear();
    }

    public void convertclick(View v) {

        Button button = (Button)findViewById(R.id.convertbtn);
        final Animation myAnim = AnimationUtils.loadAnimation(TextConverter.this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);

        String select = spinner.getSelectedItem().toString();
        switch (select) {
            case "Text to Binary": { //ok na to
                display_data.getText().clear();

                inputtext = (EditText) findViewById(R.id.inputText);
                TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
                TextView binarysimul = (TextView) findViewById(R.id.outputTextView);
                StringBuilder textbinary = new StringBuilder();

                binarysimul.setText("");
                String tex = inputtext.getText().toString();
                try {
                    //binsim.setText("");
                    binarysimul.append(Html.fromHtml("<b>Text</b>"));
                    binarysimul.append(":     \t");
                    binarysimul.append(tex + "\n\n");
                    //      binsim.append("First is to get the decimal equivalent of each character based on Decimal ASCII Chart.\n\n");
                    char c;
                    long r;

                    for (int i = 0; i < tex.length(); i++) {
                        //char tex;
                        char[] textt = {' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                                '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd',
                                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|',
                                '}', '~'};
                        c = tex.charAt(i);
                        int indexnum = getArrayIndex(textt, c);
                        binarysimul.append(c + "\t -> \t" + "Decimal Equivalent: \t\t");
                        int index = indexnum + 32;
                        binarysimul.append(index + "\n");
                        binarysimul.append("\nConverting " + index + " into binary numbers by dividing the decimal by 2 and remainder and getting remainder. Next is use the qoutient as the new dividend.Repeat the process until the quotient ends up to 0,\n\n");
                        while (index != 0) {
                            binarysimul.append(index + "/ 2 = ");
                            r = index % 2;
                            index = index / 2;
                            binarysimul.append(index + "  R  " + r + "\n");
                            binarytv.append(r + "");
                        }

                        String bin = String.valueOf(binarytv.getText());
                        binarysimul.append("\nThen getting the remainder (R) from bottom to top, the binary equivalent of " + c + " is ");
                        binarysimul.append(new StringBuffer(bin).reverse() + ".\n\n");
                        //     record=textbinary.append(" "+new StringBuffer(bin).reverse()).toString();

                        binarytv.setText("");
                    }
                    //  finbin.append(".");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String ttb = inputtext.getText().toString();

                byte[] bytesttb = ttb.getBytes();


                StringBuilder binaryttb = new StringBuilder();
                for (byte b : bytesttb) {
                    int val = b;
                    for (int i = 0; i < 8; i++) {

                        binaryttb.append((val & 128) == 0 ? 0 : 1);
                        val <<= 1;
                    }
                    record = binaryttb.append(" ").toString();
                }
                display_data.getText().clear();
                display_data.setTextSize(18);
                display_data.setText(record);


                break;
            }





            case "Binary to Text": {display_data.getText().clear();

                inputtext = (EditText) findViewById(R.id.inputText);
                StringBuilder binarybtt = new StringBuilder();

                TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
                TextView binarysimul = (TextView) findViewById(R.id.outputTextView);

                String number =inputtext.getText().toString();
                binarysimul.setText("");
                binarytv.setText("");

                if (number.matches("[0-1 ]+")) {
                    String numbers=inputtext.getText().toString().replace(" ", "");
                    try {
                        Integer teexnumber = numbers.length(); //binilang kung ilan yung binary

                        BigInteger temp;
                        BigInteger binn = new BigInteger(numbers);



                        binarysimul.append("First is to group " + numbers + " into 8-bit binary number. \n\n");

                        System.out.println("First is to group " + numbers + " into 8-bit binary number.\n\n");


                        binarytv.append("Finally, getting all the text equivalent of each 8-bit binary number, the text equivalent of " + numbers +
                                " is ");
                        System.out.println("Finally, getting all the text equivalent of each 8-bit binary number, the text equivalent of " + numbers +
                                " is ");

                        BigInteger modulo;
                        temp = binn;
                        BigInteger ten=new BigInteger ("10");
                        BigInteger zero=new BigInteger ("0");
                        //lalagay sa array bawat binary number
                        BigInteger[] numberArr = new  BigInteger[teexnumber];
                        for (int i = teexnumber - 1; i >= 0; i--) {
                            modulo = temp.mod(ten);
                            numberArr[i] = modulo;
                            temp = temp.divide(ten);
                        }

                        //kukuha ng walong binary bit
                        int k = 0;
                        int l = 0;
                        for (int j = k; j < teexnumber / 8; j++) {
                            //gagawin nang decimal yung binary
                            int finalans = 0;
                            int ans;
                            Integer ctr = 7;
                            binarysimul.append("Converting the succeeding binary into decimal number by multiplying each bit by 2 raised  to 8 minus 1 and repeating the process while decrementing the power by 1, \n\n");

                            for (int i = l; i < l + 8; i++) {
                                binarysimul.append(numberArr[i] + " x 2^" + ctr + " = ");
                                double pow = Math.pow(2, ctr);

                                System.out.println(pow);
                                ans = (int) (numberArr[i].signum() * pow);
                                binarysimul.append(ans + "\n");
                                finalans = finalans + ans;
                                ctr--;
                            }
                            l += 8;

                            binarysimul.append("\nNext, getting the sum of all the products, we get a decimal number of " + finalans +
                                    ". The text equivalent of " + finalans + " based on Decimal ASCII Chart is ");

                            //gagawin nang text yung decimal
                            char[] text = {' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4',
                                    '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                                    'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd',
                                    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|',
                                    '}', '~'};
                            binarysimul.append(text[finalans - 32] + ".\n\n");
                            //finaltext.append(text[finalans-32]+"");

                            // record=text[finalans-32]+"";
                        }






                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String btt = inputtext.getText().toString().replace(" ", "");
                    if (TextUtils.isDigitsOnly(btt)) {


                        for (int i = 0; i <= btt.length() - 8; i += 8) {
                            //  binarybtt= btt + ((char) Integer.parseInt(btt.substring(i, i + 8), 2));


                            binarybtt.append((char) Integer.parseInt(btt.substring(i, i + 8), 2));
                        }

                        record = binarybtt.append(" ").toString();


                    }






                } else if (inputtext.getText().toString().contains(" ")) {


                    record = "This is not a Binary";


                } else {

                    record ="This is not a Binary";
                }
                break;
            }








            case "Decimal to Binary": {
                inputtext = (EditText) findViewById(R.id.inputText);
                TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
                TextView binarysimul = (TextView) findViewById(R.id.outputTextView);
                StringBuilder dectobinary=new StringBuilder();
                String decimalnum = inputtext.getText().toString(); //inputted text



                if (decimalnum.matches("[0-9]+")) {

                    BigInteger num1 = new BigInteger(decimalnum);
                    binarysimul.setText("");
                    binarytv.setText("");

                    ///Float test = num1 % 1;
                    BigInteger r;
                    BigInteger num;
                    BigInteger two =new BigInteger("2");
                    num = new BigInteger(decimalnum);
                    System.out.println(num1);
                    try {
                        while (num.signum() != 0) {
                            binarysimul.append(num + "/ 2 = "); //bibring down
                            r = num.mod(two);
                            num = num.divide(two);
                            binarysimul.append(num + "  R  " + r + "\n");  //eto yung ishoshow na simulation
                            binarytv.append(r + ""); //eto yung nakabaligtad
                        }
                        // display_data.getText().clear();

                        String bin = String.valueOf(binarytv.getText()); //eto yung babaligtarin

                        record = dectobinary.append(new StringBuffer(bin).reverse()).toString();
                        Log.e("TAG", "Success");

                    } catch (Exception e) {

                    }


                } else {

                    record="Enter a valid decimal number";

                }


                break;
            }







            case "Binary to Decimal": {
                inputtext = (EditText) findViewById(R.id.inputText);
                String num = inputtext.getText().toString().toLowerCase(); //kukunin si text na nainput



                StringBuilder binarytodec = new StringBuilder();

                TextView binarydec = (TextView) findViewById(R.id.binaryTextView);
                TextView decsimul = (TextView) findViewById(R.id.outputTextView);
                binarydec.setText("");

                decsimul.setText("");



                if (num.matches("[0-1]+")) {

                    BigInteger  temp;
                    Integer counter;
                    BigInteger number = new BigInteger (num);

                    try {
                        decsimul.setText(""); //pinaka simulation
                        //  binary.setText("");
                        binarydec.setText("");
                        //fin.setText("");

                        decsimul.append("\t");
                        decsimul.append("\n\n");
                        temp = number;
                        counter = 0;
                        BigInteger ten=new BigInteger ("10");
                        BigInteger zero=new BigInteger ("0");
                        do {
                            temp = temp.divide(ten); //binibilang yung digit na inenter
                            counter++;
                        }
                        while (temp.signum()>0);

                        //Toast.makeText(MainActivity.this,number.toString(),Toast.LENGTH_SHORT).show();
                        //reset the temp
                        temp = number;

                        // make an array
                        BigInteger modulo;  //modulo is equivalent to single digit of the number.
                        BigInteger[] numberArr = new BigInteger[counter]; //i aarray lahat ng ininput

                        for (int i = counter - 1; i >= 0; i--) { //cocode ng pagaarray
                            modulo = temp.mod(ten);
                            numberArr[i] = modulo;
                            temp = temp.divide(ten);

                        }

                        // binsim.append(numberArr[2] + "\n");
                        Double finaldec = 0.0;  //simulation and computation
                        Integer ctr = counter - 1;
                        for (int i = 0; i >= 0; i++) {
                            Double dec;
                            Double pow;
                            Long decc;

                            decsimul.append(numberArr[i] + " x 2^" + ctr + " = ");
                            pow = Math.pow(2, ctr);
                            dec = numberArr[i].signum()* pow;
                            //decc = Integer.parseInt(String.valueOf(dec));
                            decc = dec.longValue();
                            decsimul.append(decc + "\n");


                            binarydec.append(decc + "+");

                            finaldec = finaldec + dec;
                            Long finaldecc = finaldec.longValue();
                            String sagot = ("Decimal number:     " + finaldecc);
                            display_data.setText(sagot);
                            record = finaldecc.toString();
                            ctr--;
                            System.out.println(binarytodec.toString());
                        }
                        //Html.fromHtml("<sup></sup>")

                    } catch (Exception ex) {
                        //   record = binarytodec.append("This is not a binary").toString();
                        // record = "This is not a binary";
                        System.out.println(binarytodec.toString());
                    }


                } else {
                    record = "This is not a binary";
                }


                break;
            }
            case "Decimal to Hexadecimal": {

                inputtext = (EditText) findViewById(R.id.inputText);
                String num = inputtext.getText().toString(); //kukunin si text na nainput


                TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
                TextView binarysimul = (TextView) findViewById(R.id.outputTextView);



                if((num.matches("[0-9]+"))){


                    Long number = Long.parseLong(num);
                    Long temp;
                    Integer counter;


                    try {
                        //Character[] dec = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
                        //dec = hexa;
                        binarysimul.setText("");
                        binarytv.setText("");
                        temp = number;
                        counter = 0;
                        do {
                            temp = temp / 16;
                            counter++;
                        }

                        while (temp > 0);
                        temp = number;
                        // make an array
                        long modulo;
                        Long[] numberArr = new Long[counter];
                        char[] strArr = new char[0];
                        char hex;
                        char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                        binarysimul.append("Dividing " + temp + " by 2, getting the hexadecimal equivalent of the remainder(R), using the qoutient as the new dividend, and repeating the process until the qoutient ends up to 0, then getting the hexadecimal equivalent" +
                                " of all the remainders (R),\n\n");
                        for (int i = counter - 1; i >= 0; i--) {
                            binarysimul.append(temp + " / 16" + " = ");
                            modulo = temp % 16;
                            hex = hexa[(int) modulo];
                            temp = temp / 16;
                            binarysimul.append(temp + "\tR\t" + modulo + " -> Hexadecimal equivalent: \t" + hex + "\n\n");
                            System.out.println(hex);
                        }


                        temp = number;

                        for (int i = 0; i <= counter - 1; i++) {
                            modulo = temp % 16;
                            hex = hexa[(int) modulo];
                            temp = temp / 16;
                            binarytv.append("" + hex);
                        }
                        binarysimul.append("And lastly, getting the hexadecimals from bottom to top, the hexadecimal equivalent of " + num + " is ");
                        String rev = binarytv.getText().toString();

                        String answer = (new StringBuffer(rev).reverse() + "");
                        record = answer;
                        //Html.fromHtml("<sup></sup>")

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    record="Enter a valid decimal number";
                }





                break;
            }
            case "Hexadecimal to Decimal": {
                inputtext = (EditText) findViewById(R.id.inputText);
                String num = inputtext.getText().toString(); //kukunin si text na nainput


                StringBuilder binarytodec = new StringBuilder();

                TextView binarytv = (TextView) findViewById(R.id.binaryTextView);
                TextView binarysimul = (TextView) findViewById(R.id.outputTextView);


                String hexa = inputtext.getText().toString();

                if(hexa.matches("[0-9a-fA-F_]+")){


                    try {
                        binarysimul.setText("");
                        binarytv.setText("");
                        binarysimul.append("First is to get the decimal equivalent of each character of \"" + hexa + "\".\n\n");
                        char c;
                        String d;
                        char[] hexe = new char[0];
                        int ctr = hexa.length() - 1;
                        int dec;
                        int finaldec = 0;
                        double pow;
                        int min = 1;
                        for (int i = 0; i < hexa.length(); i++) {
                            char[] hexx = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                            c = Character.toUpperCase(hexa.charAt(i));
                            int indexnum = getArrayIndexes(hexx, c);
                            binarysimul.append(c + "\t -> \t" + "Decimal Equivalent: \t\t");
                            binarysimul.append(indexnum + "\n\n");
                            binarysimul.append("Multiplying " + indexnum + " by 16 raised to the number of characters (" + hexa.length() + ") minus " + min + ",\n");
                            pow = Math.pow(16, ctr);
                            dec = (int) (indexnum * pow);
                            binarysimul.append(indexnum + " x 16^" + ctr + " = " + dec + "\n\n");
                            finaldec = finaldec + dec;
                            ctr--;
                            min += 1;
                        }
                        binarysimul.append("Finally, adding up all the products, the decimal equivalent of \"" + hexa + "\" is " + finaldec + ".\n\n\n");
                        String answer = (finaldec + "");
                        record = answer;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else{
                    record="Enter a valid hexadecimal";
                }



                break;
            }
        }


        display_data.setTextSize(18);
        display_data.setText(record);

        if(inputtext.getText().toString().isEmpty()){
            simul.setEnabled(false);
        }
        else if (display_data.getText().toString().equals("This is not a Binary")){
            simul.setEnabled(false);
        }
        else if (display_data.getText().toString().equals("Enter a valid decimal number")){
            simul.setEnabled(false);
        }
        else if (display_data.getText().toString().equals("Enter a valid hexadecimal")){
            simul.setEnabled(false);
        }



        else{simul.setEnabled(true);}




    }

    private int getArrayIndex(char[] textt, char c) {
        int k = 0;
        for (int i = 0; i < textt.length; i++) {
            if (textt[i] == c) {
                k = i;
            }
        }
        return k;
    }

    private int getArrayIndexes(char[] hexx, char c) {
        int k=0;
        for (int i=0; i< hexx.length; i++){
            if (hexx[i] == c) {
                k=i;
            }
        }
        return k;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
