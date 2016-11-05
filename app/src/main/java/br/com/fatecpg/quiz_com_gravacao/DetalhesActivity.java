package br.com.fatecpg.quiz_com_gravacao;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
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

    public void carregaDetallhes(){

    }

    public void abrePopup(String tipoNome){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(tipoNome);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // set dialog message
        alertDialogBuilder
                .setMessage("Digite a nota:")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Salvar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        salvar(input.getText().toString());
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

    public void salvar(String txt){
        Toast.makeText(this, "Nota: " + txt, Toast.LENGTH_LONG).show();
    }

}
