package Models;
// Generated 09-sep-2017 12:20:57 by Hibernate Tools 4.3.1



/**
 * Ordenador generated by hbm2java
 */
public class Ordenador  implements java.io.Serializable {


     private Integer idOrdenador;
     private String sistemaOperativo;
     private String nombreUsuario;

    public Ordenador() {
    }

    public Ordenador(String sistemaOperativo, String nombreUsuario) {
       this.sistemaOperativo = sistemaOperativo;
       this.nombreUsuario = nombreUsuario;
    }
   
    public Integer getIdOrdenador() {
        return this.idOrdenador;
    }
    
    public void setIdOrdenador(Integer idOrdenador) {
        this.idOrdenador = idOrdenador;
    }
    public String getSistemaOperativo() {
        return this.sistemaOperativo;
    }
    
    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }
    public String getNombreUsuario() {
        return this.nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }




}


