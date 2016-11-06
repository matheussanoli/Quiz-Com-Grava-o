package br.com.fatecpg.quiz_com_gravacao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaDetallhes("Matematica");
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

    public void setRbText(int rbId, float nota){
        RadioButton rb = (RadioButton) findViewById(rbId);
        rb.setText(rb.getText().toString().substring(0,3)+" "+String.format("%,.2f", nota));
    }

    public String getNomeDisciplina(){
        TextView tvDisciplina = (TextView) findViewById(R.id.tvDiciplina);
        return tvDisciplina.getText().toString();
    }

    public void carregaDetallhes(String nomeDisciplina){
        SharedPreferences pref = this.getSharedPreferences("br.com.fatecpg.quiz", Context.MODE_PRIVATE);

        TextView tv = (TextView) findViewById(R.id.tvDiciplina);
        tv.setText(this.getIntent().getStringExtra("Disciplina"));
        setRbText(R.id.rbP1,pref.getFloat(getNomeDisciplina()+"_P1", 0));
        setRbText(R.id.rbP2,pref.getFloat(getNomeDisciplina()+"_P2", 0));
        setRbText(R.id.rbTP,pref.getFloat(getNomeDisciplina()+"_TP", 0));

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
        SharedPreferences pref = this.getSharedPreferences("br.com.fatecpg.quiz", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(nomeDisciplina+"_"+tipoNota, Float.parseFloat(nota.replace(",", ".")));
        editor.commit();
    }

    public void voltar(View view){
        Intent i = new Intent(DetalhesActivity.this, ListarDisciplinas.class);
        startActivity(i);
    }

}
