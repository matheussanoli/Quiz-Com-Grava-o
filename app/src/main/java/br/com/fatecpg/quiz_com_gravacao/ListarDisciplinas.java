package br.com.fatecpg.quiz_com_gravacao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class    ListarDisciplinas extends AppCompatActivity {
    File[] dirFiles;
    ArrayList<String> listItens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_disciplina);

        ArrayList<String> files = new ArrayList<>();
        File dir = getFilesDir();
        dirFiles = dir.listFiles();
        for (int i = 1; i < dirFiles.length; i++) {
            if(!dirFiles[i].isDirectory()) {
                files.add(dirFiles[i].getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItens);
        ListView list = (ListView) findViewById(R.id.disciplinas);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i++;
                Intent intent = new Intent(getApplicationContext(), DetalhesActivity.class);
                intent.putExtra("file", i);
                startActivity(intent);
            }
        });

        iniciarCadastro();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> files = new ArrayList<>();
        File dir = getFilesDir();
        dirFiles = dir.listFiles();
        for (int i = 0; i < dirFiles.length; i++) {
            if(!dirFiles[i].isDirectory()) {
                files.add(dirFiles[i].getName());
            }
        }
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files);
        ListView list = (ListView)findViewById(R.id.disciplinas);
        list.setAdapter(aa);
    }



    public void iniciarCadastro() {
        Button cadDisciplina = (Button) findViewById(R.id.cadDisciplina);
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
        Disciplina disc = new Disciplina();

        try{
            disc.nome = String.valueOf(disciplina);
        }catch(Exception e){

        }

        String filename = disc.nome;
        FileOutputStream output;

        try{
            output = openFileOutput(filename, Context.MODE_PRIVATE);

            output.close();
        }catch(Exception ex){
            Toast.makeText(this,"Erro ao gravar arquivo: " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(getApplicationContext(), DetalhesActivity.class);
        i.putExtra("nome", filename);
        startActivity(i);
    }
}
