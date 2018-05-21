/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Musteri;
import org.hibernate.Session;

/**
 *
 * @author SONER
 */
public class MusteriYonetici {
    private final JTable musteriTablo;
    private final static String SORGU_KALIP = "from Musteri m";
    private Session session;
    private final Vector<String> sutunlar = new Vector<>();
    private Vector<Object> satir;
    private final DefaultTableModel model;
    public String baglanti;
    public MusteriYonetici(JTable musteriTablo) {
        this.musteriTablo = musteriTablo;
        sutunlar.add("Öğrenci ID");
        sutunlar.add("Öğrenci No");
        sutunlar.add("Ad Soyad");
        sutunlar.add("Şehir");
        sutunlar.add("Tel No");
        model=(DefaultTableModel)musteriTablo.getModel();
        model.setColumnIdentifiers(sutunlar);
    }
    
    public String baglantiKontrol(){
        boolean durum = session.isConnected();
        if(durum==true)
            return "Bağlandı";
        else 
            return "Bağlantı Kesildi";
    }

    public void musteriGetir(String aranan, String filtre) {
        String sorguMetin = "";
        if (filtre.equalsIgnoreCase("ad")) {
            sorguMetin = SORGU_KALIP + " where m.ad like '%" + aranan + "%'";
        } else if (filtre.equalsIgnoreCase("adres")) {
            sorguMetin = SORGU_KALIP + " where m.soyad like '%" + aranan + "%'";
        }
        session.beginTransaction();
        List musterilerList = session.createQuery(sorguMetin).list();
        session.getTransaction().commit();
        musteriGoster(musterilerList);

    }
    
    public void musteriListele(){
        String sorguMetin = SORGU_KALIP;
        session.beginTransaction();
        List musterilerList = session.createQuery(sorguMetin).list();
        session.getTransaction().commit();
        musteriGoster(musterilerList);
    }
    
    public void musteriEkle(String gelenAd, String gelenSoyad,String gelenAdres,String gelenTelno){
        Musteri yeniMusteri = new Musteri();
        yeniMusteri.setAd(gelenAd);
        yeniMusteri.setSoyad(gelenSoyad);
        yeniMusteri.setAdres(gelenAdres);
        yeniMusteri.setTelno(gelenTelno);
        session.save(yeniMusteri);
    }
    
    public void musteriSil(int gelenId){
        session.beginTransaction();
        Musteri silinecekMusteri=(Musteri) session.get(Musteri.class, gelenId);
        session.delete(silinecekMusteri);
        session.getTransaction().commit();
        
    }
    
    public void musteriGuncelle(int gelenId,String gelenAd, String gelenSoyad,String gelenAdres,String gelenTelno){
        session.beginTransaction();
        Musteri guncellenecekMusteri=(Musteri) session.get(Musteri.class, gelenId);
        guncellenecekMusteri.setAd(gelenAd);
        guncellenecekMusteri.setSoyad(gelenSoyad);
        guncellenecekMusteri.setAdres(gelenAdres);
        guncellenecekMusteri.setTelno(gelenTelno);
        session.merge(guncellenecekMusteri);
        session.getTransaction().commit();
        
    }

    public void ac() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void kapat() {
        session.close();
    }

    private void musteriGoster(List<Musteri> musterilerList) {
        model.getDataVector().removeAllElements();
        for (Musteri gelenMusteri : musterilerList) {
            satir=new Vector();
            satir.add(gelenMusteri.getMusteriid());
            satir.add(gelenMusteri.getAd());
            satir.add(gelenMusteri.getSoyad());
            satir.add(gelenMusteri.getAdres());
            satir.add(gelenMusteri.getTelno());
            model.addRow(satir);
        }
    }
}
