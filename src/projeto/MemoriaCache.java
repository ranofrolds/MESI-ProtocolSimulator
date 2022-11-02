package src.projeto;

public class MemoriaCache{
    //um bloco da memória RAM é alocado a uma linha da memoria cache
    Linha linhas[];
    int qntLinhas;

    public MemoriaCache(){
        qntLinhas=10;
        linhas=new Linha[qntLinhas];
    }
    
    public Linha povoarLinha(Linha novaLinha){
        
        for(int i=0;i<linhas.length;i++){ //povoa 2 linhas do bloco //quando a RAM não ta cheia  
            if(linhas[i]==null){
                linhas[i]=new Linha();
                linhas[i].dadosBloco = novaLinha.dadosBloco;
                linhas[i].numeroBloco= novaLinha.numeroBloco;
                linhas[i].infoMESI=novaLinha.infoMESI;
                return null;
            }
            else if(linhas[i].numeroBloco==novaLinha.numeroBloco){
                linhas[i].dadosBloco = novaLinha.dadosBloco;
                linhas[i].numeroBloco= novaLinha.numeroBloco;
                linhas[i].infoMESI=novaLinha.infoMESI;
                return null;
            }
        }
        //nao tem espaço na cache, todas linhas ocupados
        /*Aqui vai ser o FIFO, primeiro ele acessa a CACHE do processador X
        depois de acessada ele vai remover o primeiro elemento e armazenar em
        uma carry, após isso ele vai adicionar o novo elemento no final da fila*/
        Linha dadoRemovido=FIFO(novaLinha);
        return dadoRemovido;      
        

    }

    public Linha FIFO(Linha novaLinha){
        Linha primeira = linhas[0]; //atualizar a linha que saiu para RAM

        for(int i =1;i<linhas.length-1;i++){ //a fila anda uma casa pra frente
            linhas[i-1]=linhas[i];
        }
        
        linhas[linhas.length-1]=novaLinha;

        return primeira;
    }

}