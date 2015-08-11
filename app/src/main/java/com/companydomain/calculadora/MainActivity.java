package com.companydomain.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewOperacion;
    private TextView textViewResultado;
    private TextView textViewSigno;
    private ArrayList<String> operacion;
    private int operador1;
    private int operador2;
    private double respuesta;
    private boolean flagSigno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewOperacion = (TextView)findViewById(R.id.textViewOperacion);
        textViewResultado = (TextView)findViewById(R.id.textViewResultado);
        textViewSigno = (TextView)findViewById(R.id.textViewSigno);
        operacion = new ArrayList<String>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void refrescarCampoOperacion(){
        String texto = "";

        for(int i=0; i<operacion.size(); i++){
            texto += operacion.get(i);
        }
        textViewOperacion.setText(texto);
    }

    public void refrescarCampoResultado(){
        String texto = "";

        for(int i=0; i<operacion.size(); i++){
            texto += operacion.get(i);
        }

        textViewResultado.setText(texto + " = ");

        int ultimo = texto.length()-1;

        if( texto.charAt(ultimo) == ' ' ) {
            textViewOperacion.setText("ERROR");
        }
    }

    public void mostrarOperacion(){

        String texto = "";

        for( int i=0; i<operacion.size(); i++ ){
            texto += operacion.get(i);
        }

        //Empieza con -
        if( texto.startsWith(" - ") && ( !texto.contains(" x ") && !texto.contains(" † ") ) ){
            String temp =  texto.replaceAll(" - ", "&&");
            String temp2 = temp.replaceAll(" † ", " - ");
            String temp3 = temp2.replaceAll("&&", " † ");
            texto = temp3.replaceFirst(" † ", "");
            flagSigno = true;
            //textViewOperacion.setText("Inicia en menos" + "");
        }

        //Sumar
        if( !texto.contains(" - ") && !texto.contains(" x ") && !texto.contains(" / ") ){
            String[] valores = texto.split(" † ");

            respuesta = 0;

            for( int i=0; i<valores.length; i++ ){
                respuesta += Double.parseDouble(valores[i]);
            }

            if( flagSigno ){
                textViewOperacion.setText("-" + respuesta + "");
                flagSigno = false;
            }
            else
                textViewOperacion.setText(respuesta + "");

        }

        //Restar
        else if( !texto.contains(" † ") && !texto.contains(" x ") && !texto.contains(" / ") ){
            String[] valores = texto.split(" - ");

            respuesta = 0;
            int contador = 0;

            for( int i=0; i<valores.length; i++ ){
                if( contador == 0) {
                    respuesta = Double.parseDouble(valores[i]);
                }

                else{
                    respuesta -= Double.parseDouble(valores[i]);
                }
                contador++;
            }

            if( flagSigno ){
                textViewOperacion.setText("-" + respuesta + "");
                flagSigno = false;
            }
            else
                textViewOperacion.setText(respuesta + "");
        }

        //Multiplicar
        else if( !texto.contains(" † ") && !texto.contains(" - ") && !texto.contains(" / ") ){
            String[] valores = texto.split(" x ");

            respuesta = 0;
            int contador = 0;

            for( int i=0; i<valores.length; i++ ){
                if( contador == 0)
                    respuesta = Double.parseDouble(valores[i]);

                else{
                    respuesta *= Double.parseDouble(valores[i]);
                }
                contador++;
            }

            textViewOperacion.setText( respuesta  +  "" );
        }

        //Dividir
        else if( !texto.contains(" † ") && !texto.contains(" - ") && !texto.contains(" x ") ){
            String[] valores = texto.split(" / ");

            respuesta = 0;
            int contador = 0;

            if( !texto.contains("0") ){
                for( int i=0; i<valores.length; i++ ){
                    if( contador == 0)
                        respuesta = Double.parseDouble(valores[i]);
                    else{
                        respuesta /= Double.parseDouble(valores[i]);
                    }
                    contador++;
                }
            }
            textViewOperacion.setText( respuesta  +  "" );
            if( texto.contains("0") ){
                textViewOperacion.setText( "Indefinido" );
            }
        }

        //Sin signo
        else if( !texto.contains(" † ") && !texto.contains(" - ") && !texto.contains(" / ") && !texto.contains(" x ") ){

            respuesta = Double.parseDouble( texto );

            textViewOperacion.setText( respuesta  +  "" );
        }

        //Todas las operaciones
        else{
            if( texto.contains(" / ") )
                operarDiv();
            else if( texto.contains(" x ") )
                operarMul(texto);
            else if( texto.contains( " † " ) )
                operarSum( texto );
            else{
                soloRestar(texto);
            }
        }
    }

    public void operarMul( String resultadoText ){

        String texto = resultadoText;

        String[] valores = texto.split(" x ");
        String texto2 = "";
        String texto3 = "";
        String resultadoTexto = "";

        if( texto.contains(" x ") ){
            String n1="";
            String n2="";
            String n3="1"; // Debe empezar en 1 para el primer ciclo
            String n3a = "";
            double n1n = 0;
            double n2n = 0;
            double productoTemp = 0;

            for( int i=0; i<valores.length; i++ ){

                char[] cad = valores[i].toCharArray();

                //Caso i=0
                if(i==0){
                    n1 = "1";
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }

                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") )
                        texto2 = valores[i].replaceFirst(" " + n3, " ");
                    else{
                        texto2 = valores[i].replaceFirst(n3, "");
                    }
                    texto3 = texto2;
                }

                //Caso i=total array
                else if(i == valores.length -1){
                    n1 = n3;
                    n2 = "";
                    n3 = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);
                    productoTemp = n1n*n2n;

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") )
                        texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");

                    else{
                        texto2 = valores[i].replaceFirst(n2, productoTemp + "");
                    }
                    texto3 = texto2;
                }

                else{
                    n1 = n3;
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }
                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    //Continuando
                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);
                    productoTemp = n1n*n2n;

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") ){
                        texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");

                        if(texto2.contains(" † ") || texto2.contains(" - ") )
                            texto3 = texto2.replaceFirst(" " + n3, " ");
                        else
                            texto3 = texto2.replaceFirst(n3, "");
                    }
                    else{
                        texto2 = valores[i].replaceFirst(n2, productoTemp + "");

                        if(texto2.contains(" † ") || texto2.contains(" - ") )
                            texto3 = texto2.replaceFirst(" " + n3, " ");
                        else {
                            texto3 = texto2.replaceFirst(productoTemp+"", "");
                            n3=texto2;
                        }
                    }
                }

                resultadoTexto += texto3;

            }

            if( resultadoTexto.contains(" † "))
                operarSum(resultadoTexto);

            else{
                soloRestar(resultadoTexto);
            }
        }
        else{
            operarSum(texto);
        }
    }

    public void operarDiv(){

        String texto = "";

        for(int i=0; i<operacion.size(); i++){
            texto += operacion.get(i);
        }

        String[] valores = texto.split(" / ");
        String texto2 = "";
        String texto3 = "";
        String resultadoTexto = "";

        if( texto.contains(" / ") ){
            String n1="";
            String n2="";
            String n3="1";
            String n3a = "";
            double n1n = 0;
            double n2n = 0;
            double productoTemp = 0;

            for( int i=0; i<valores.length; i++ ){

                char[] cad = valores[i].toCharArray();

                //Caso i=0
                if(i==0){
                    n1 = "1";
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }

                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") || valores[i].contains(" x ") )
                        texto2 = valores[i].replaceFirst(" " + n3, " ");
                    else{
                        texto2 = valores[i].replaceFirst(n3, "");
                    }
                    texto3 = texto2;
                }

                //Caso i=total array
                else if(i == valores.length -1){
                    n1 = n3;
                    n2 = "";
                    n3 = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);

                    if( n2n == 0 || n2.equals("0"))
                        indeterminacion();

                    else
                        productoTemp = n1n/n2n;

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") || valores[i].contains(" x ") )
                        texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");

                    else{
                        texto2 = valores[i].replaceFirst(n2, productoTemp + "");
                    }
                    texto3 = texto2;
                }

                else{
                    n1 = n3;
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }
                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    //Continuando
                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);

                    if( n2n == 0 || n2.equals("0"))
                        indeterminacion();

                    else
                        productoTemp = n1n/n2n;

                    if( valores[i].contains(" † ") || valores[i].contains(" - ") || valores[i].contains(" x ")){
                        texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");

                        if(texto2.contains(" † ") || texto2.contains(" - ") || valores[i].contains(" x "))
                            texto3 = texto2.replaceFirst(" " + n3, " ");
                        else
                            texto3 = texto2.replaceFirst(n3, "");
                    }
                    else{
                        texto2 = valores[i].replaceFirst(n2, productoTemp + "");

                        if(texto2.contains(" † ") || texto2.contains(" - ") || valores[i].contains(" x ") )
                            texto3 = texto2.replaceFirst(" " + n3, " ");
                        else {
                            texto3 = texto2.replaceFirst(productoTemp+"", "");
                            n3=texto2;
                        }
                    }
                }

                resultadoTexto += texto3;
            }

            if( resultadoTexto.contains(" x "))
                operarMul(resultadoTexto);

            else if( resultadoTexto.contains(" † "))
                operarSum(resultadoTexto);

            else{
                soloRestar(resultadoTexto);
            }

            //textViewOperacion.setText( resultadoTexto  +  " Division" );
        }

        else{
            operarSum(texto);
        }
    }

    public void indeterminacion(){
        textViewOperacion.setText( "Ind."  +  " #/0" );
    }

    public void soloRestar( String resultadoTexto ){
        String texto = resultadoTexto;
        String[] valores = texto.split(" - ");

       respuesta = 0;
        int contador = 0;

        for( int i=0; i<valores.length; i++ ){
            if( contador == 0)
                respuesta = Double.parseDouble(valores[i]);
            else{
                respuesta -= Double.parseDouble(valores[i]);
            }
            contador++;
        }

        if( flagSigno ){
            textViewOperacion.setText("-" + respuesta + "");
            flagSigno = false;
        }
        else
            textViewOperacion.setText(respuesta + "");
        //textViewOperacion.setText( resultadoTexto + " Solo restar");
    }

    public void operarSum( String resultadoText ){
        String texto = resultadoText;

        if( texto.contains(" - ") && texto.contains(" † ") ){

            String[] valores = texto.split(" † ");
            String texto2 = "";
            String texto3 = "";
            String resultadoTexto = "";

            String n1="";
            String n2="";
            String n3="";
            String n3a = "";
            double n1n = 0;
            double n2n = 0;
            double productoTemp = 0;
            boolean carry = false;
            boolean carry2 = false;
            boolean flag = false;

            for( int i=0; i<valores.length; i++ ){

                char[] cad = valores[i].toCharArray();

                //Caso i=0
                if(i==0){
                    n1 = "";
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }

                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    if( valores[i].contains(" - ") ) {
                        texto2 = valores[i].replaceFirst(" " + n3, " ");
                        carry = true;
                        flag = true;
                    }
                    else{
                        texto2 = valores[i].replaceFirst(n3, "");
                    }
                    texto3 = texto2;
                }

                //Caso i=total array
                else if(i == valores.length -1){
                    n1 = n3;
                    n2 = "";
                    n3 = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);

                    if( carry ){
                        productoTemp = -n1n+n2n;

                        if( productoTemp < 0 ){
                            productoTemp = Math.abs( productoTemp );
                        }
                        else{
                            carry = false;
                            carry2 = true;
                        }
                    }
                    else{
                        productoTemp = n1n+n2n;
                    }

                    if( valores[i].contains(" - ") ) {
                        if (carry2) {
                            texto2 = valores[i].replaceFirst(n2 + " ", "- " + productoTemp + " ");
                        }
                        else{
                            texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");
                        }
                        carry2 = false;
                    }

                    else{
                        if( carry2 ){
                            texto2 = valores[i].replaceFirst(n2, "- " + productoTemp + "");
                        }
                        else{
                            texto2 = valores[i].replaceFirst(n2, productoTemp + "");
                        }
                        carry2 = false;

                    }
                    texto3 = texto2;
                }

                else{
                    n1 = n3;
                    n2 = "";
                    n3 = "";
                    n3a = "";

                    for( int j=0; j<cad.length && cad[j] != ' '; j++ ){
                        n2 += cad[j];
                    }

                    //Guardar en la variable n3 el valor n1 del siguiente ciclo

                    for( int k=cad.length-1; k>=0 && cad[k] != ' '; k-- ){
                        n3a += cad[k];
                    }
                    StringBuilder builder=new StringBuilder(n3a);
                    n3 = builder.reverse().toString();

                    //Continuando
                    n1n = Double.parseDouble(n1);
                    n2n = Double.parseDouble(n2);

                    if( carry ){
                        productoTemp = -n1n+n2n;

                        if( productoTemp < 0 ){
                            productoTemp = Math.abs( productoTemp );
                        }
                        else{
                            carry = false;
                            carry2 = true;
                        }
                    }
                    else{
                        productoTemp = n1n+n2n;
                    }

                    if( valores[i].contains(" - ") ){
                        if( carry2 ){
                            texto2 = valores[i].replaceFirst(n2 + " ", "- " + productoTemp + " ");

                            if(texto2.contains(" - ") )
                                texto3 = texto2.replaceFirst(" - " + n3, " - ");
                            else
                                texto3 = texto2.replaceFirst(n3, "");

                            carry2 = false;
                        }

                        else{
                            texto2 = valores[i].replaceFirst(n2 + " ", productoTemp + " ");

                            if(texto2.contains(" - ") )
                                texto3 = texto2.replaceFirst(" " + n3, " ");
                            else
                                texto3 = texto2.replaceFirst(n3, "");
                        }
                        carry = true;
                        flag = true;
                    }
                    else{
                        if( carry2 ){
                            texto2 = valores[i].replaceFirst(n2, productoTemp + "");

                            if( texto2.contains(" - ") )
                                texto3 = texto2.replaceFirst(" " + n3, " ");
                            else {
                                texto3 = texto2.replaceFirst(productoTemp+"", "- ");
                                n3=texto2;
                            }
                            carry2 = false;
                        }
                        else{
                            texto2 = valores[i].replaceFirst(n2, productoTemp + "");

                            if( texto2.contains(" - ") )
                                texto3 = texto2.replaceFirst(" " + n3, " ");
                            else {
                                texto3 = texto2.replaceFirst(productoTemp+"", "");
                                n3=texto2;
                            }
                        }
                    }
                }

                resultadoTexto += texto3;

            }

            if( !texto.contains(" - ") && texto.contains(" † ") ){
                operarSum2(resultadoTexto);
            }

            if( resultadoTexto.contains(" - - ") || resultadoTexto.contains(" † ") ){
                String temp = resultadoTexto;
                resultadoTexto = temp.replaceAll( " - - ", " † " );

                if( resultadoTexto.contains(" - ") )
                    operarSum( resultadoTexto );
                else
                    operarSum2(resultadoTexto);
            } else{
                soloRestar(resultadoTexto);
                //textViewOperacion.setText( resultadoTexto  +  "" );
            }

            //textViewOperacion.setText( resultadoTexto  +  " Fin sumar" );
        }

        else if( resultadoText.contains(" † ") && !resultadoText.contains(" - ") ){
            operarSum2(resultadoText);
        }
        else
            soloRestar(resultadoText);
    }

    public void operarSum2( String resultadoTexto ){

        if( !resultadoTexto.contains(" x ") && !resultadoTexto.contains(" / ") ){
            String[] valores = resultadoTexto.split(" † ");

            respuesta = 0;
            for( int i=0; i<valores.length; i++ ){
                respuesta += Double.parseDouble(valores[i]);
            }

            textViewOperacion.setText( respuesta + "");
        }
    }

    public void eventoBtnC(View view){
        if( textViewOperacion.getText().equals("") || textViewOperacion.getText() == null ){
            textViewOperacion.setText("Esta limpio");
        }
        else {
            int ultimo = operacion.size() - 1;
            operacion.remove( ultimo );
            refrescarCampoOperacion();
        }
    }

    public void eventoBtnCE(View view){
        operacion.clear();
        refrescarCampoOperacion();
    }

    public void eventoBtn0(View view){
        operacion.add("0");
        refrescarCampoOperacion();
    }

    public void eventoBtn1(View view){
        operacion.add("1");
        refrescarCampoOperacion();
    }

    public void eventoBtn2(View view){
        operacion.add("2");
        refrescarCampoOperacion();
    }

    public void eventoBtn3(View view){
        operacion.add("3");
        refrescarCampoOperacion();
    }

    public void eventoBtn4(View view){
        operacion.add("4");
        refrescarCampoOperacion();
    }

    public void eventoBtn5(View view){
        operacion.add("5");
        refrescarCampoOperacion();
    }

    public void eventoBtn6(View view){
        operacion.add("6");
        refrescarCampoOperacion();
    }

    public void eventoBtn7(View view){
        operacion.add("7");
        refrescarCampoOperacion();
    }

    public void eventoBtn8(View view){
        operacion.add("8");
        refrescarCampoOperacion();
    }

    public void eventoBtn9(View view){
        operacion.add("9");
        refrescarCampoOperacion();
    }

    public void eventoBtnSum(View view){
        operacion.add(" † ");
        refrescarCampoOperacion();
    }

    public void eventoBtnRes(View view){
        operacion.add(" - ");
        refrescarCampoOperacion();
    }

    public void eventoBtnMul(View view){
        operacion.add(" x ");
        refrescarCampoOperacion();
    }

    public void eventoBtnDiv(View view){
        operacion.add(" / ");
        refrescarCampoOperacion();
    }

    public void eventoBtnIgu(View view){
        mostrarOperacion();
        refrescarCampoResultado();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
