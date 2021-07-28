package com.example.tarea_reconocimiento_texto_ml_kit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.telephony.ims.ImsManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

public class MainActivity extends AppCompatActivity {

    TextView txtTextoObtenido;
    ImageView imgImagenObtenida;
    Button btnObtenerImagen;
    Button btnObtenerTexto;
    Uri imgUriAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTextoObtenido=(TextView)findViewById(R.id.txtTextoObtenido);
        txtTextoObtenido.setMovementMethod(new ScrollingMovementMethod());
        imgImagenObtenida=(ImageView)findViewById(R.id.imgVista);
        btnObtenerImagen=(Button)findViewById(R.id.btnAgregarImagen);
        btnObtenerTexto=(Button)findViewById(R.id.btnReconocerTexto);


    }
    public void reconocerTexto(View view){
        if(imgUriAux!=null) {
            convertirATexto(imgUriAux);
        }else {
            Toast.makeText(getApplicationContext(), "Debe seleccionar una Imagen.", Toast.LENGTH_LONG).show();
        }
    }

    public void convertirATexto(Uri imagenUri){
        InputImage inpImg;
        Uri imgUri=imagenUri;
        TextRecognizer txtRecognizer;
        txtRecognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        try {
            inpImg=InputImage.fromFilePath(this,imgUri);
            Task<Text> resultado=txtRecognizer.process(inpImg).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(@NonNull Text text) {

                    txtTextoObtenido.setText(text.getText());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al Reconocer el Texto:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void escogerImagen(View view){


        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicación"),10);
    }
    @Override
    protected void onActivityResult (int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri imgUri =data.getData();
            imgUriAux=imgUri;
            //Aqui debes llamar a una funcion para reconocer el texto
            imgImagenObtenida.setImageURI(imgUri);
        }
    }

}