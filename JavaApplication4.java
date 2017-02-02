package javaapplication4;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Paramentro 0 Lettera da analizzare (maiuscola)
 * Paramentro 1 parola dalla quale partire per la scansione
 * Paramentro 2 path del file dove salvare i risultati
 * 
 * @author Aquilino Donato
 */
public class JavaApplication4 {

    public static void main(String[] args) throws Exception {
	
        String link_dizionario = "http://dizionari.corriere.it/dizionario_italiano/";
        String lettera = args[0];
        String parola = args[1];
        System.out.print(parola+",");
        URL url_parola = new URL(link_dizionario+lettera+"/"+parola+".shtml");
        
        ArrayList<ArrayList<String>> parole = new ArrayList<>();
        
        //apro il file in scrittura
        FileWriter w;
        w=new FileWriter(args[2]);

        BufferedWriter b;
        b=new BufferedWriter (w);
        b.write(parola+",");
        
        do {
                                            
                //apro il file in lettura dal sito
                BufferedReader in = new BufferedReader(
                new InputStreamReader(url_parola.openStream()));
                
                //struttura che salva in memoria le parole e i relativi tag delle definizioni
                ArrayList<String> def = new ArrayList<>();

                String inputLine;
                //scorro fino a quando non trovo l'inizio delle definizioni
                while ((inputLine = in.readLine()) != null)
                {
                    if(inputLine.equals("<!-- Definizioni - Inizio -->"))
                      break;       
                }
                //salvo le definizioni e gli altri attributi in una lista
                while (((inputLine = in.readLine()) != null) && (!inputLine.equals("<!-- Definizioni - Fine -->")))
                {
                       def.add(inputLine);               
                }
                //System.out.println(def);
                //ricavo l'anno o il secolo della parola
                String[] temp = def.get(def.size()-4).split("<li><p>� ");
                
                //nel caso non ci sia l'anno relativo alla parola mette un "-"
                if(temp.length == 1) {
                    //nel caso in cui una pagina sia scritta una riga si e una no
                    if(temp[0].equals("</div>")) {
                        b.write("-doppialinea");
                        System.out.print("doppialinea");
                    } else {                           
                        b.write("-");
                    }
                    b.write("\n");
                    System.out.println();
                } else {
                    //se la definizione inizia con sec. o a.
                    if(temp[1].startsWith("sec.") || temp[1].startsWith("a.")) {
                        String[] anno = temp[1].split("</p></li>");
                        b.write(anno[0]);
                        System.out.println(anno[0]);
                        b.write("\n");
                        parole.add(def);
                    }
                    else {
                        //cerca nella penultima definizione
                        String[] temp2 = def.get(def.size()-5).split("<li><p>� ");
                        if(temp2.length == 1) {
                            b.write("-");
                            b.write("\n");
                            System.out.println();
                        } else if(temp2[1].startsWith("sec.") || temp2[1].startsWith("a.")) {
                                    String[] anno = temp2[1].split("</p></li>");
                                    b.write(anno[0]);
                                    System.out.println(anno[0]);
                                    b.write("\n");
                                    parole.add(def);
                         } else {
                            b.write("-");
                            b.write("\n");
                            System.out.println();
                        }
                        
                    }
                }
                //scrivo su file l'anno/secolo
                
                in.close();

                //cerco la prossima parola su vocabolario
                for(int i = 0; i < def.size(); i++) {
                    if(def.get(i).startsWith("<li><a class=\"def-attivo\" href=\"#\">"))
                    {
                        //controllo se ho finito le parole
                        if(def.get(i+1).equals("</ul>")) {
                           b.close();
                           return;
                        }
                        else {
                            //elimino il prefisso
                            String new_word = def.get(i+1).replace("<li><a href=\"", "");
                            //nel caso il sorgente abbia una riga si e una no (vedi alcantara)
                            if(new_word.isEmpty()){
                               new_word = def.get(i+2).replace("<li><a href=\"", "");
                            }
                            //splitto per ottenere la parola (che sarà in posizione 0)
                            String[] n_word = new_word.split(".shtml");
                            System.out.print(n_word[0]+",");
                            
                            url_parola = new URL(link_dizionario+lettera+"/"+n_word[0]+".shtml");
                           
                            b.write(n_word[0]+",");
                            //System.out.println(n_word[0]);
                        }
                    }
                }

        } while (true);
        
    }
}
