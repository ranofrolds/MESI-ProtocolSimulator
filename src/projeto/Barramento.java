package src.projeto;

public class Barramento {
    public Processador processadores[];
    MemoriaPrincipal mem;
    int QNTD_PROCESSADORES;

    public Barramento(int n, MemoriaPrincipal mem){
        processadores = new Processador[n];
        QNTD_PROCESSADORES=n;
        this.mem = mem;
    }

    public void adicionarProcessador(Processador cpu){
        for(int pos = 0; pos < QNTD_PROCESSADORES; pos++){
            if(processadores[pos] == null){
                processadores[pos] = cpu;
                break;
            }
        }
    }

    public void atualizarShared(int numeroBloco,int labelCacheAtual){
        for(int j=0; j<QNTD_PROCESSADORES;j++){
            Processador p=processadores[j];
            if(p==processadores[labelCacheAtual]){ //caso seja a mesma cache ele pula
                continue;
            }
            for(int i=0; i<p.cache.qntLinhas;i++){ 
                if(p.cache.linhas[i]!=null&&numeroBloco==p.cache.linhas[i].numeroBloco &&p.cache.linhas[i].infoMESI!='I' ){ //caso seja cacha diferente
                    p.cache.linhas[i].infoMESI='S';
                }
                
            }
        }   
    }

    public void invalidarCaches(int numeroBloco,int labelCacheAtual){
        for(int j=0; j<QNTD_PROCESSADORES;j++){
            Processador p=processadores[j];
            if(p==processadores[labelCacheAtual]){ //caso seja a mesma cache ele pula
                continue;
            }
            for(int i=0; i<p.cache.qntLinhas;i++){ 
                if(p.cache.linhas[i]!=null&&numeroBloco==p.cache.linhas[i].numeroBloco){ //caso seja cacha diferente
                    p.cache.linhas[i].infoMESI='I';
                }
                
            }
        }   
    }
}
