/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.util;

import java.io.Serializable;

/**
 *
 * @author Uellington Damasceno
 */
public class ArvoreAVL implements Serializable{

    private Node raiz;
    
    private class Node {

        private Comparable dados;
        private int balanceamento;
        private Node pai;
        private Node direita;
        private Node esquerda;

        public Node(Comparable dados) {
            this.dados = dados;
            this.balanceamento = 0;
        }
        
        @Override
        public String toString(){
            return dados.toString();
        }
        
        public int getBalanceamento() {
            return balanceamento;
        }

        public void setBalanceamento(int balanceamento) {
            this.balanceamento = balanceamento;
        }

        public Node getPai() {
            return pai;
        }

        public void setPai(Node pai) {
            this.pai = pai;
        }

        public Node getEsquerda() {
            return esquerda;
        }

        public void setEsquerda(Node esquerda) {
            this.esquerda = esquerda;
        }

        public Comparable getDados() {
            return dados;
        }

        public void setDados(Comparable dados) {
            this.dados = dados;
        }

        public Node getDireita() {
            return direita;
        }

        public void setDireita(Node direita) {
            this.direita = direita;
        }
    }

    public boolean estaVazia() {
        return (raiz == null);
    }
    
    public Object buscarPalavra(Comparable palavra) {
        return buscarPalavra(raiz, palavra);
    }

    private Object buscarPalavra(Node palavraAtual, Comparable palavraBuscada) {
        if (palavraAtual == null || palavraBuscada == null) {
            return null;
        } else {
            if (palavraBuscada.compareTo(palavraAtual.getDados()) == 0) {
                //System.out.println("Encontrado :::: "  + palavraAtual);
                return palavraAtual.getDados();
            } else if (palavraBuscada.compareTo(palavraAtual.getDados()) < 0) {
                System.out.println("esquerda");
                return buscarPalavra(palavraAtual.getEsquerda(), palavraBuscada);
            } else {
                System.out.println("direita");
                return buscarPalavra(palavraAtual.getDireita(), palavraBuscada);
            }
        }
    }

    public void inserir(Comparable palavra) {
        Node palavraAinserir = new Node(palavra);
        inserir(this.raiz, palavraAinserir);
    }
    
    public boolean contem(Comparable palavra){
        return estaVazia() ? false : (buscarPalavra(palavra) != null);
    }
    
    private void inserir(Node palavraAtual, Node palavraInserir) {

        if (estaVazia()) {
            this.raiz = palavraInserir;
        } else {
            if (palavraInserir.getDados().compareTo(palavraAtual.getDados()) < 0) {
                if (palavraAtual.getEsquerda() == null) {
                    palavraAtual.setEsquerda(palavraInserir);
                    palavraInserir.setPai(palavraAtual);
                    verBalanceamento(palavraAtual);
                } else {
                    inserir(palavraAtual.getEsquerda(), palavraInserir);
                }
            } else if (palavraInserir.getDados().compareTo(palavraAtual.getDados()) > 0) {
                if (palavraAtual.getDireita() == null) {
                    palavraAtual.setDireita(palavraInserir);
                    palavraInserir.setPai(palavraAtual);
                    verBalanceamento(palavraAtual);
                } else {
                    inserir(palavraAtual.getDireita(), palavraInserir);
                }
            }
        }
    }

    private void verBalanceamento(Node atual) {
        setBalanceamento(atual);
        int balanceamento = atual.getBalanceamento();
        if (balanceamento == -2) {
            if (altura(atual.getEsquerda().getEsquerda()) >= altura(atual.getEsquerda().getDireita())) {
                atual = rotacaoDireita(atual);
            } else {
                atual = duplaRotacaoEsquerdaDireita(atual);
            }
        } else if (balanceamento == 2) {
            if (altura(atual.getDireita().getDireita()) >= altura(atual.getDireita().getEsquerda())) {
                atual = rotacaoEsquerda(atual);
            } else {
                atual = duplaRotacaoDireitaEsquerda(atual);
            }
        }
        if (atual.getPai() != null) {
            verBalanceamento(atual.getPai());
        } else {
            this.raiz = atual;
        }
    }

    public void remover(Comparable palavra) {
        remover(this.raiz, palavra);
    }

    private void remover(Node atual, Comparable palavra) {
        if (estaVazia()) {
        } else {
            if (atual.getDados().compareTo(palavra) > 0) {
                remover(atual.getEsquerda(), palavra);

            } else if (atual.getDados().compareTo(palavra) < 0) {
                remover(atual.getDireita(), palavra);

            } else if (atual.getDados().equals(palavra)) {
                removerNoEncontrado(atual);
            }
        }
    }

    private void removerNoEncontrado(Node aRemover) {
        Node aux;

        if (aRemover.getEsquerda() == null || aRemover.getDireita() == null) {
            if (aRemover.getPai() == null) {
                this.raiz = null;
                return;
            }
            aux = aRemover;
        } else {
            aux = sucessor(aRemover);
            aRemover.setDados(aux.getDados());
        }

        Node p;
        if (aux.getEsquerda() != null) {
            p = aux.getEsquerda();
        } else {
            p = aux.getDireita();
        }

        if (p != null) {
            p.setPai(aux.getPai());
        }

        if (aux.getPai() == null) {
            this.raiz = p;
        } else {
            if (aux == aux.getPai().getEsquerda()) {
                aux.getPai().setEsquerda(p);
            } else {
                aux.getPai().setDireita(p);
            }
            verBalanceamento(aux.getPai());
        }
    }

    private Node rotacaoEsquerda(Node inicial) {

        Node direita = inicial.getDireita();
        direita.setPai(inicial.getPai());

        inicial.setDireita(direita.getEsquerda());

        if (inicial.getDireita() != null) {
            inicial.getDireita().setPai(inicial);
        }

        direita.setEsquerda(inicial);
        inicial.setPai(direita);

        if (direita.getPai() != null) {

            if (direita.getPai().getDireita() == inicial) {
                direita.getPai().setDireita(direita);

            } else if (direita.getPai().getEsquerda() == inicial) {
                direita.getPai().setEsquerda(direita);
            }
        }

        setBalanceamento(inicial);
        setBalanceamento(direita);

        return direita;
    }

    private Node rotacaoDireita(Node inicial) {

        Node esquerda = inicial.getEsquerda();
        esquerda.setPai(inicial.getPai());

        inicial.setEsquerda(esquerda.getDireita());

        if (inicial.getEsquerda() != null) {
            inicial.getEsquerda().setPai(inicial);
        }

        esquerda.setDireita(inicial);
        inicial.setPai(esquerda);

        if (esquerda.getPai() != null) {

            if (esquerda.getPai().getDireita() == inicial) {
                esquerda.getPai().setDireita(esquerda);

            } else if (esquerda.getPai().getEsquerda() == inicial) {
                esquerda.getPai().setEsquerda(esquerda);
            }
        }

        setBalanceamento(inicial);
        setBalanceamento(esquerda);

        return esquerda;
    }

    private Node duplaRotacaoEsquerdaDireita(Node inicial) {
        inicial.setEsquerda(rotacaoEsquerda(inicial.getEsquerda()));
        return rotacaoDireita(inicial);
    }

    private Node duplaRotacaoDireitaEsquerda(Node inicial) {
        inicial.setDireita(rotacaoDireita(inicial.getDireita()));
        return rotacaoEsquerda(inicial);
    }

    private Node sucessor(Node aRemover) {
        if (aRemover.getDireita() != null) {
            Node r = aRemover.getDireita();
            while (r.getEsquerda() != null) {
                r = r.getEsquerda();
            }
            return r;
        } else {
            Node pai = aRemover.getPai();
            while (pai != null && aRemover == pai.getDireita()) {
                aRemover = pai;
                pai = aRemover.getPai();
            }
            return pai;
        }
    }

    private int altura(Node aVerificar) {
        if (aVerificar == null) {
            return -1;
        }
        if (aVerificar.getEsquerda() == null && aVerificar.getDireita() == null) {
            return 0;
        } 
        else if (aVerificar.getEsquerda() == null) {
            return 1 + altura(aVerificar.getDireita());
        } 
        else if (aVerificar.getDireita() == null) {
            return 1 + altura(aVerificar.getEsquerda());
        } 
        else {
            return 1 + Math.max(altura(aVerificar.getEsquerda()), altura(aVerificar.getDireita()));
        }
    }

    private void setBalanceamento(Node no) {
        no.setBalanceamento(altura(no.getDireita()) - altura(no.getEsquerda()));
    }
}
