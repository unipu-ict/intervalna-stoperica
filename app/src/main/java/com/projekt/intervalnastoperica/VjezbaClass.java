package com.projekt.intervalnastoperica;



public class VjezbaClass {

    private int id;
    private String ime;
    private int priprema;
    private int vjezba;
    private int odmor;
    private int ponavljanja;
    private int setovi;

    public int getID() {
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime){
        this.ime = ime;
    }

    public int getPriprema() {
        return priprema;
    }

    public void setPriprema(int priprema){
        this.priprema = priprema;
    }

    public int getVjezba() {
        return vjezba;
    }

    public void setVjezba(int vjezba){
        this.vjezba = vjezba;
    }

    public int getOdmor() {
        return odmor;
    }

    public void setOdmor(int odmor){
        this.odmor = odmor;
    }

    public int getPonavljanja() {
        return ponavljanja;
    }

    public void setPonavljanja(int ponavljanja){
        this.ponavljanja = ponavljanja;
    }

    public int getSetovi() {
        return setovi;
    }

    public void setSetovi(int setovi){
        this.setovi = setovi;
    }

    public int getTrajanje() {
        return (this.priprema + (this.ponavljanja*(this.vjezba + this.odmor)))*this.setovi;
    }

}
