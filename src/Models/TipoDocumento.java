package Models;
// Generated 09-sep-2017 12:20:57 by Hibernate Tools 4.3.1



/**
 * TipoDocumento generated by hbm2java
 */
public class TipoDocumento  implements java.io.Serializable {


     private String idTipoDocumento;
     private String descripcion;

    public TipoDocumento() {
    }

    public TipoDocumento(String idTipoDocumento, String descripcion) {
       this.idTipoDocumento = idTipoDocumento;
       this.descripcion = descripcion;
    }
   
    public String getIdTipoDocumento() {
        return this.idTipoDocumento;
    }
    
    public void setIdTipoDocumento(String idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}

