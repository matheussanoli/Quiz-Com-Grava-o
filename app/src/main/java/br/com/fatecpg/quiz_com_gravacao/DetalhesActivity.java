package br.com.fatecpg.quiz_com_gravacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    public String getRbNota(int rbId){
        RadioButton rb = (RadioButton) findViewById(rbId);
        return rb.getText().toString().replace("P1: ","").replace("P2: ","").replace("TP: ","");
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
            calcularMedia();
            lerArq.close();
        }catch (Exception ex){
            System.out.println("erro: " + ex.getMessage());
        }
    }

    public void abrePopup(final String tipoNota){
        final Context context;
        final TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(tipoNota);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialogBuilder
                .setMessage("Digite a nota:")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Salvar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        float valid = Float.parseFloat(input.getText().toString());

                        if ( valid < 0 || valid > 10){
                            input.setError("Preencha todos os campos com valores de 0 á 10!");
                            input.requestFocus();
                        }
                        else{
                            salvar(tvDisciplina.getText().toString() , tipoNota, input.getText().toString());
                            dialog.cancel();
                        }
                        /*salvar(tvDisciplina.getText().toString() , tipoNota, input.getText().toString());
                        dialog.cancel();*/
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();

    }

    public void inserirNota(View view){
        abrePopup(rbSelected().getText().toString().substring(0,2));
    }

    public void salvar(String nomeDisciplina, String tipoNota,String nota){
        FileOutputStream output;

        try{
            output = openFileOutput(filename, Context.MODE_APPEND);


            output.write((tipoNota+": " + nota + "\n").getBytes());

            output.close();
            carregaDetallhes();
        }catch(Exception ex){
            Toast.makeText(this,"Erro ao gravar arquivo: " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void calcularMedia(){
        String status;
        float media = 0;
        float p1 = Integer.parseInt(getRbNota(R.id.rbP1));
        float p2 = Integer.parseInt(getRbNota(R.id.rbP2));
        float tp = Integer.parseInt(getRbNota(R.id.rbTP));
        media = (p1+p2+tp)/3;
        TextView tv = (TextView) findViewById(R.id.media);
        tv.setText("Media: "+String.valueOf(media));

        TextView st = (TextView) findViewById(R.id.status);

        if (media >= 6){

            status = "Aprovado";
            st.setTextColor(Color.parseColor("#00FF05"));
        }

        else{
            status = "Reprovado";
            st.setTextColor(Color.parseColor("#FF0000"));
        }


        st.setText(String.valueOf(status));
    }

    public void excluirDisciplina(View view){
        final TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(tvDisciplina.getText().toString());
        alertDialogBuilder
                .setMessage("Tem certeza que deseja excluir essa disciplina?")
                .setCancelable(false)
                .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Context context = getApplicationContext();
                        context.deleteFile(filename);
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }

    public void voltar(View view){
        Intent i = new Intent(DetalhesActivity.this, ListarDisciplinas.class);
        startActivity(i);
    }

}
