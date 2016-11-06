package br.com.fatecpg.quiz_com_gravacao;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class    ListarDisciplinas extends AppCompatActivity {
    File[] dirFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_disciplina);

        ArrayList<String> files = new ArrayList<>();
        File dir = getFilesDir();
        dirFiles = dir.listFiles();
        for (int i = 0; i < dirFiles.length; i++) {
            if(!dirFiles[i].isDirectory()) {
                files.add(dirFiles[i].getName());
            }
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files);

        ListView list = (ListView)findViewById(R.id.disciplinas);
        list.setAdapter(adapter2);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String filename = ListarDisciplinas.this.dirFiles[i].getName();
                AlertDialog.Builder fileDialog = new AlertDialog.Builder(ListarDisciplinas.this);
                fileDialog.setTitle(filename);
                try{
                    BufferedReader in = new BufferedReader(new FileReader(ListarDisciplinas.this.dirFiles[i]));
                    StringBuilder text = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    in.close();
                    fileDialog.setMessage(text);
                }catch(Exception ex){
                    fileDialog.setMessage("Erro ao carregar arquivo: "+ex.getLocalizedMessage());
                }
                fileDialog.setNeutralButton("Fechar", null);
                fileDialog.show();
            }
        });
    }

    public void cadDisciplina(final String tipoNome){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(tipoNome);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        // set dialog message
        alertDialogBuilder
                .setMessage("Digite a Disciplina:")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Salvar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        salvarDisciplina(input.getText().toString());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // show it
        alertDialogBuilder.show();
    }


    public void salvarDisciplina(String disciplina){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(disciplina, "");
        editor.commit();
    }

    public void carregaDisciplina(){

    }

}
