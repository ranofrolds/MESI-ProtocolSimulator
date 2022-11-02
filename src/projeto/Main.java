package src.projeto;

import java.util.*;


public class Main{

    public static void main(String[] args) {
        MemoriaPrincipal  mem = new MemoriaPrincipal();
        Barramento bus=new Barramento(3,mem);

        System.out.println("Preenchendo Valores aleatorios na Memoria RAM entre 0 e 100mil");
        mem.preencheMemoria(); //preenche a memoria com valores aleatorios  de 0 - 100 000
        
        System.out.println("Quantidade de elementos Memoria RAM: " + mem.tamanho);
        mem.printarMemoria();

        Processador p1 = new Processador(0,bus, mem.tamanho/2);
        Processador p2 = new Processador(1,bus, mem.tamanho/2);
        Processador p3 = new Processador(2,bus, mem.tamanho/2);
        //cada processador tem uma cache
        //os processadores utilizam a mesma memória

        bus.adicionarProcessador(p1);
        bus.adicionarProcessador(p2);
        bus.adicionarProcessador(p3);

        int exit=0, choose=0;
        Scanner sc=new Scanner(System.in);


        do{
            System.out.println("====== MENU DE EXECUCAO DO PROGRAMA======");
            System.out.println("Escolha um processador:");
            System.out.println("(1) Processador 1\n(2) Processador 2\n(3) Processador 3\n(4) Printar Todas Caches\n(5) Printar Memoria RAM\n(0) EXIT");
            choose=sc.nextInt();
            switch(choose){
                case 1:
                    //processador 1
                    System.out.println("Deseja Ler ou Escrever um dado na Memoria?\n(1) Ler\n(2) Escrever\n(3) Printar Cache");
                    choose=sc.nextInt();
                    p1.chamarOperacao(choose, sc);
                    break;
                case 2:
                    //processador 2
                    System.out.println("Deseja Ler ou Escrever um dado na Memoria?\n(1) Ler\n(2) Escrever\n(3) Printar Cache");
                    choose=sc.nextInt();
                    p2.chamarOperacao(choose, sc);
                    break;
                case 3:
                    //processador 3
                    System.out.println("Deseja Ler ou Escrever um dado na Memoria?\n(1) Ler\n(2) Escrever\n(3) Printar Cache");
                    choose=sc.nextInt();
                    p3.chamarOperacao(choose, sc);
                    break;
                case 4:
                    for(int i=0; i<bus.QNTD_PROCESSADORES; i++){
                        bus.processadores[i].printarCache();
                    }
                    break;
                case 5:
                    mem.printarMemoria();
                    break;
                case 0:
                    exit=1;
                    break;
                default:
                    System.out.println("Funcao Desconhecida!");
            }
            

        }while(exit!=1);
        
        System.out.println("FIM DA EXECUCAO!");
        sc.close();
    }
}
