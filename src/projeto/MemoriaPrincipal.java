package src.projeto;

import java.util.*;

public class MemoriaPrincipal {
    
    /*  Nossa Memoria Principal tem 525 blocos de 2 dados cada um
     */

    int dados[];
    int tamanho;

    public MemoriaPrincipal(){
        tamanho=1050;
    }

    public void preencheMemoria(){
        //cada bloco tem 2 elementos
        Random gerador = new Random();

        dados=new int[this.tamanho];

        for(int i=0; i<this.tamanho;i++){
            dados[i]=gerador.nextInt(100000);
        }
    }

    public void printarMemoria(){
        int i=0;
        for(i=0; i<=tamanho-1; i++){
            System.out.println("Linha: "+i+" Dado: "+dados[i]);
        }
    }
   
}
