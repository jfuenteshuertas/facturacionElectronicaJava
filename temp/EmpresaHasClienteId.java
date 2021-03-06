package Models;
// Generated 10-jul-2017 16:17:01 by Hibernate Tools 4.3.1



/**
 * EmpresaHasClienteId generated by hbm2java
 */
public class EmpresaHasClienteId  implements java.io.Serializable {


     private int empresaIdEmpresa;
     private String clienteNumero;

    public EmpresaHasClienteId() {
    }

    public EmpresaHasClienteId(int empresaIdEmpresa, String clienteNumero) {
       this.empresaIdEmpresa = empresaIdEmpresa;
       this.clienteNumero = clienteNumero;
    }
   
    public int getEmpresaIdEmpresa() {
        return this.empresaIdEmpresa;
    }
    
    public void setEmpresaIdEmpresa(int empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }
    public String getClienteNumero() {
        return this.clienteNumero;
    }
    
    public void setClienteNumero(String clienteNumero) {
        this.clienteNumero = clienteNumero;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof EmpresaHasClienteId) ) return false;
		 EmpresaHasClienteId castOther = ( EmpresaHasClienteId ) other; 
         
		 return (this.getEmpresaIdEmpresa()==castOther.getEmpresaIdEmpresa())
 && ( (this.getClienteNumero()==castOther.getClienteNumero()) || ( this.getClienteNumero()!=null && castOther.getClienteNumero()!=null && this.getClienteNumero().equals(castOther.getClienteNumero()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getEmpresaIdEmpresa();
         result = 37 * result + ( getClienteNumero() == null ? 0 : this.getClienteNumero().hashCode() );
         return result;
   }   


}


