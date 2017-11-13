package Models;
// Generated 10-jul-2017 16:17:01 by Hibernate Tools 4.3.1



/**
 * Cliente generated by hbm2java
 */
public class Cliente  implements java.io.Serializable {


     private String numero;
     private short tipoDocumento;
     private String direccion;
     private String email;
     private String nombre;
     private String ubigeo;

    public Cliente() {
    }

	
    public Cliente(String numero, short tipoDocumento, String email, String nombre) {
        this.numero = numero;
        this.tipoDocumento = tipoDocumento;
        this.email = email;
        this.nombre = nombre;
    }
    public Cliente(String numero, short tipoDocumento, String direccion, String email, String nombre, String ubigeo) {
       this.numero = numero;
       this.tipoDocumento = tipoDocumento;
       this.direccion = direccion;
       this.email = email;
       this.nombre = nombre;
       this.ubigeo = ubigeo;
    }
   
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public short getTipoDocumento() {
        return this.tipoDocumento;
    }
    
    public void setTipoDocumento(short tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUbigeo() {
        return this.ubigeo;
    }
    
    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }




}

