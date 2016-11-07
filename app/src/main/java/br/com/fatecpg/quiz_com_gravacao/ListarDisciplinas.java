package br.com.fatecpg.quiz_com_gravacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class    ListarDisciplinas extends AppCompatActivity {
    File[] dirFiles;
    ArrayList<String> listItens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_disciplina);

        String disciplina = "";
        listViews(disciplina);

        botaoCadastro();

        }

    public void listViews(final String disciplina) {
        ListView list = (ListView) findViewById(R.id.disciplinas);

        ArrayList<String> files = new ArrayList<>();
        File dir = getFilesDir();
        dirFiles = dir.listFiles();
        for (int i = 0; i < dirFiles.length; i++) {
            if(!dirFiles[i].isDirectory()) {
              files.add(dirFiles[i].getName());
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItens);
        list.setAdapter(adapter);
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
                        Log.i("Test", "text : "+text+" : end");
                        text.append('\n');
                    }
                    in.close();
                    fileDialog.setMessage(text);
                }catch(Exception ex){
                    fileDialog.setMessage("Erro ao carregar arquivo: "+ex.getLocalizedMessage());
                }

                Intent intent = new Intent(ListarDisciplinas.this, DetalhesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nome", disciplina);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void botaoCadastro() {
        final Button cadDisciplina = (Button) findViewById(R.id.cadDisciplina);
        cadDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadDisciplina();
            }
        });
    }


    public void cadDisciplina(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Cadastrar Disciplina");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );

        alertDialogBuilder
                .setMessage("Digite a Disciplina:")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
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

        alertDialogBuilder.show();
    }

    public void salvarDisciplina(String disciplina){
        Collections.addAll(listItens, disciplina);

        SharedPreferences pref = this.getSharedPreferences("br.com.fatecpg.quiz", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nomeDisciplina", disciplina);
        editor.commit();
    }
}
