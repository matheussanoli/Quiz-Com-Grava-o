package br.com.fatecpg.quiz_com_gravacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class DetalhesActivity extends AppCompatActivity {

    private float media = 0;
    File[] dirFiles;
    String filename;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);


    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaDetallhes();
    }

    public RadioButton rbSelected(){
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgNotas);
        return (RadioButton) findViewById(rg.getCheckedRadioButtonId());
    }

    public void isRbSelected(View view){
        RadioButton rb =(RadioButton) view;
        Button btnIN = (Button) findViewById(R.id.btnInserirNota);
        if(rb.isChecked()){
            btnIN.setEnabled(true);
        }else{
            btnIN.setEnabled(false);
        }
    }

    public void setRbText(int rbId, String nota){
        RadioButton rb = (RadioButton) findViewById(rbId);
        rb.setText(nota);
    }

    public String getNomeDisciplina(){
        TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        Intent i = getIntent();
        String nome = i.getStringExtra("nome");
        tvDisciplina.setText(nome);
        return tvDisciplina.getText().toString();
    }

    public void carregaDetallhes(){
        ArrayList<String> files = new ArrayList<>();
        File dir = getFilesDir();
        dirFiles = dir.listFiles();

        TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        Intent intent = getIntent();
        i = intent.getIntExtra("file",0);
        filename =dirFiles[i].getName();
        tvDisciplina.setText(filename);

        try {
            BufferedReader lerArq = new BufferedReader(new FileReader(dirFiles[i]));
            String line;
            while ((line = lerArq.readLine()) != null) {
                Toast.makeText(this, "li: "+ line, Toast.LENGTH_LONG).show();

                if(line.substring(0,2).equals("P1")){
                    setRbText(R.id.rbP1, line);
                }
                else if(line.substring(0,2).equals("P2")){
                    setRbText(R.id.rbP2, line);
                }
                else if(line.substring(0,2).equals("TP")){
                    setRbText(R.id.rbTP, line);
                }
            }
            lerArq.close();
        }catch (Exception ex){

        }

        /*
        setRbText(R.id.rbP1,pref.getFloat(getNomeDisciplina()+"_P1", 0));
        setRbText(R.id.rbP2,pref.getFloat(getNomeDisciplina()+"_P2", 0));
        setRbText(R.id.rbTP,pref.getFloat(getNomeDisciplina()+"_TP", 0));
*/
    }

    public void abrePopup(final String tipoNota){
        final TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(tipoNota);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // set dialog message
        alertDialogBuilder
                .setMessage("Digite a nota:")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Salvar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        salvar(tvDisciplina.getText().toString() , tipoNota, input.getText().toString());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // show it
        alertDialogBuilder.show();

    }

    public void inserirNota(View view){
        abrePopup(rbSelected().getText().toString().substring(0,2));
    }

    public void salvar(String nomeDisciplina, String tipoNota,String nota){
        /*SharedPreferences pref = this.getSharedPreferences("br.com.fatecpg.quiz", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(nomeDisciplina+"_"+tipoNota, Float.parseFloat(nota.replace(",", ".")));
        editor.commit();
        */
        FileOutputStream output;

        try{
            output = openFileOutput(filename, Context.MODE_PRIVATE);


            output.write((tipoNota+": " + nota + "\n").getBytes());

            output.close();
            carregaDetallhes();
        }catch(Exception ex){
            Toast.makeText(this,"Erro ao gravar arquivo: " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public float calcular(float add, float remover){
        if(media != 0 ){
            media = (media - remover) + add;
        }
        return media;
    }

    public void excluirDisciplina(View view){
        SharedPreferences pref = this.getSharedPreferences("br.com.fatecpg.quiz", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("");
    }

    public void voltar(View view){
        Intent i = new Intent(DetalhesActivity.this, ListarDisciplinas.class);
        startActivity(i);
    }

}
