package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import br.org.fatec.softinc.models.User;

public class TelaPrincipal extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView textMenssagem;
    Button buttonLogoutPrincipal;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        textMenssagem = (TextView)findViewById(R.id.textMenssagem);
        buttonLogoutPrincipal = (Button)findViewById(R.id.buttonLogoutPrincipal);
        buttonLogoutPrincipal.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            mDatabase.child("users").child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        User user = new User();
                        Gson gson = new Gson();
                        user = gson.fromJson(String.valueOf(task.getResult().getValue()),User.class);
                        //System.out.println(String.valueOf(task.getResult().getValue()));
                        textMenssagem.setText("Olá " + user.nome);
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        mAuth.signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}