package src.projeto;

import java.util.Scanner;

public class Processador {
    int label;
    MemoriaCache cache;
    Barramento bus;
    
    int QNTD_BLOCO;
    
    public Processador(int label,Barramento bus,int qnt){
        this.label=label;
        cache = new MemoriaCache();
        this.bus=bus;
        QNTD_BLOCO=qnt;
    }

    public void povoarLinhaCache(Linha novaLinha){

        Linha dadoRemovido=cache.povoarLinha(novaLinha);

        if(dadoRemovido != null){//atualizar na RAM
            /*A gente vai procurar o bloco na RAM e adicionar o dado novo*/
            if(dadoRemovido.infoMESI!='I'){
                bus.mem.dados[dadoRemovido.numeroBloco*2]=dadoRemovido.dadosBloco[0];
                bus.mem.dados[(dadoRemovido.numeroBloco*2)+1]=dadoRemovido.dadosBloco[1];
            }else{
                System.out.println("O dado não pode ser adicionado na RAM por ser INVALIDO");
            }
        }
    }

    public int[] acessaBloco(int numeroBloco){ //retorna dados do bloco que voce acessou //n == numero do bloco
    
        int dadosBloco[] = new int[2]; //aloca espaço para o vetor
        
        if(numeroBloco<QNTD_BLOCO){
            //bloco=dados[(n-1)*2]...dados[n*2+2]
            int inicioBloco=(numeroBloco)*2; //linha de inicio do bloco
            int finalBloco=inicioBloco+1; //linha do final do bloco
            
            for(int i=inicioBloco; i<finalBloco+1; i++){
                dadosBloco[i-inicioBloco]=bus.mem.dados[i];
            }

            return dadosBloco;
        }
        else{
            System.out.println("Não existe tal bloco.");
            dadosBloco=null;
            return dadosBloco;
        }
        
    }

    public void chamarOperacao(int operation, Scanner sc1){
        Scanner sc= sc1;
        
        //leitura
        if(operation ==1 ){
            System.out.println("Digite uma linha da RAM:");
            int nLinha= sc.nextInt();
            int bloco=0;
            int posBloco=2;//justamente pra dar erro e ser obrigado a entrar no if
            if(nLinha%2 == 0){ //caso a linha seja par
                bloco = nLinha/2;
                posBloco = 0;
            }
            else if(nLinha%2 == 1){ //caso a linha seja impar
                bloco = (nLinha-1)/2;
                posBloco = 1;
            }
            else{ //caso o número não seja par nem impar
                System.out.println("Erro ao achar o bloco");
                posBloco = 2; //não existe, vai dar erro
            }

            String localizacaoDado=verificarLocalizacaoDado(bloco);// "READHIT", "CACHE", "RAM"
            System.out.println(localizacaoDado);
            Linha novaLinha=leitura(localizacaoDado, bloco, posBloco);
            if(localizacaoDado.equals("READMISS 0") ||localizacaoDado.equals("READMISS 1") ||localizacaoDado.equals("READMISS 2")){
                bus.atualizarShared(bloco, posBloco);
            }

            povoarLinhaCache(novaLinha);     
        }

        //escrita
        else if(operation ==2){ 
            System.out.println("Digite uma linha da RAM:");
            int nLinha= sc.nextInt();
            int bloco=0;
            int posBloco=2;//justamente pra dar erro e ser obrigado a entrar no if
            if(nLinha%2 == 0){ //caso a linha seja par
                bloco = nLinha/2;
                posBloco = 0;
            }
            else if(nLinha%2 == 1){ //caso a linha seja impar
                bloco = (nLinha-1)/2;
                posBloco = 1;
            }
            else{ //caso o número não seja par nem impar
                System.out.println("Erro ao achar o bloco");
                posBloco = 2; //não existe, vai dar erro
            }

            System.out.println("Digite o novo valor:");
            int novoValor= sc.nextInt();
            
            String localizacaoDado=verificarLocalizacaoDado(bloco); //dentro da função de escrita precisamos ver se foi pego na RAM ou em outra cache
            if(localizacaoDado.equals("READHIT")){
                System.out.println("WRITE HIT");
            }else{
                System.out.println("WRITE MISS");
            }

            Linha novaLinha=leitura(localizacaoDado, bloco, posBloco);
            

            novaLinha.dadosBloco[posBloco]=novoValor;
            novaLinha.infoMESI='M';

            bus.invalidarCaches(novaLinha.numeroBloco,this.label);
            
            povoarLinhaCache(novaLinha);
            
        }
        else if(operation==3){
            printarCache();
        }
        else{
            System.out.println("Operacao Invalida!");
        }
    }

    public String verificarLocalizacaoDado(int bloco){
        //checar na propria cache
        for(int i=0; i<cache.qntLinhas;i++){
            if(cache.linhas[i]!=null){
                if(bloco==cache.linhas[i].numeroBloco&&(cache.linhas[i].infoMESI=='M' ||cache.linhas[i].infoMESI=='E'||cache.linhas[i].infoMESI=='S')){
                    return "READHIT";
                }
            }   
        }
        //checar outros processadores
        for(int j=0; j<bus.QNTD_PROCESSADORES;j++){
            
            Processador p=bus.processadores[j];
            if(p!=bus.processadores[label]){ //caso seja a mesma cache ele pula
                for(int i=0; i<p.cache.qntLinhas;i++){ 
                    
                    if(p.cache.linhas[i]!=null && bloco==p.cache.linhas[i].numeroBloco &&(p.cache.linhas[i].infoMESI=='M' ||p.cache.linhas[i].infoMESI=='E'||p.cache.linhas[i].infoMESI=='S')){ //caso seja cacha diferente
                        
                        return ("READMISS "+ j);
                    }
                    
                }
            }
           
        }      
        return "READMISS RAM";
    }

    public Linha leitura(String localizacaoDado, int numeroBloco, int posBloco){
       
        Linha novaLinha=new Linha();
        //caso ele não ache a informação em nenhuma cache ele procura direto na RAM
        if(localizacaoDado.equals("READMISS RAM")){
            int dadosBloco[] = acessaBloco(numeroBloco);

            novaLinha.dadosBloco[0]=dadosBloco[0];
            novaLinha.dadosBloco[1]=dadosBloco[1];
            novaLinha.numeroBloco=numeroBloco;
            novaLinha.infoMESI='E';
            return novaLinha;
        }
        //caso a linha ser lida esteja na CACHE
        else if(localizacaoDado.equals("READHIT")){
            for(int i=0;i<cache.linhas.length;i++){ //povoa 2 linhas do bloco //quando a RAM não ta cheia  
                if(cache.linhas[i]!=null&&cache.linhas[i].numeroBloco==numeroBloco){
                    novaLinha.dadosBloco[0]=cache.linhas[i].dadosBloco[0];
                    novaLinha.dadosBloco[1]=cache.linhas[i].dadosBloco[1];
                    if(cache.linhas[i].infoMESI == 'M'){
                        novaLinha.infoMESI='M';
                    }else if(cache.linhas[i].infoMESI == 'E'){
                        novaLinha.infoMESI='E';
                    }else if(cache.linhas[i].infoMESI == 'S'){
                        novaLinha.infoMESI='S';
                    }else{
                        System.out.println("ERRO FATAL READHIT MESI");
                    }
                    novaLinha.numeroBloco=cache.linhas[i].numeroBloco;
                   
                    return novaLinha;
                }
            }
        }
        else if(localizacaoDado.equals("READMISS 0") ||localizacaoDado.equals("READMISS 1") ||localizacaoDado.equals("READMISS 2")){ 
            //outraCache = a cache que possui a mesma informação que a cache atual
            //i == copiaLinha = percorre todas as linhas da outra cache que conte a informação
            int numeroProcessador=Character.getNumericValue(localizacaoDado.toCharArray()[9]);

            
            MemoriaCache outraCache=bus.processadores[numeroProcessador].cache;
            
            for(Linha i:outraCache.linhas){
                if(i.numeroBloco==numeroBloco){
                    //encontrou o bloco na cache do outro processador
                    novaLinha.numeroBloco=i.numeroBloco;
                    novaLinha.dadosBloco[0]=i.dadosBloco[0];
                    novaLinha.dadosBloco[1]=i.dadosBloco[1];

                    //atualiza protocolo MESI das duas
                    novaLinha.infoMESI='S'; //da cache que voce selecionou
                    i.infoMESI='S'; //da outra cache
                    return novaLinha;
                }
            }

            
        }
        return null;
    }

    void printarCache(){
        System.out.println("======= CACHE PROCESSADOR "+(label+1)+" =======");

        int j=0;
        for(Linha i:cache.linhas){
            if(i!=null){
                System.out.println("Linha["+j+"] = "+i.dadosBloco[0]+" "+i.dadosBloco[1]+" | MESI = "+i.infoMESI);
            }
            else{
                System.out.println("Linha["+j+"] = "+"null");
            }
            
            j++;
        }
    }
}
