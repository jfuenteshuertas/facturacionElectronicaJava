package Models;
// Generated 09-sep-2017 12:20:57 by Hibernate Tools 4.3.1



/**
 * ComprobantePagoId generated by hbm2java
 */
public class ComprobantePagoId  implements java.io.Serializable {


     private String serieComprobante;
     private int empresaIdEmpresa;
     private String tipoComprobanteIdTipoComprobante;

    public ComprobantePagoId() {
    }

    public ComprobantePagoId(String serieComprobante, int empresaIdEmpresa, String tipoComprobanteIdTipoComprobante) {
       this.serieComprobante = serieComprobante;
       this.empresaIdEmpresa = empresaIdEmpresa;
       this.tipoComprobanteIdTipoComprobante = tipoComprobanteIdTipoComprobante;
    }
   
    public String getSerieComprobante() {
        return this.serieComprobante;
    }
    
    public void setSerieComprobante(String serieComprobante) {
        this.serieComprobante = serieComprobante;
    }
    public int getEmpresaIdEmpresa() {
        return this.empresaIdEmpresa;
    }
    
    public void setEmpresaIdEmpresa(int empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }
    public String getTipoComprobanteIdTipoComprobante() {
        return this.tipoComprobanteIdTipoComprobante;
    }
    
    public void setTipoComprobanteIdTipoComprobante(String tipoComprobanteIdTipoComprobante) {
        this.tipoComprobanteIdTipoComprobante = tipoComprobanteIdTipoComprobante;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ComprobantePagoId) ) return false;
		 ComprobantePagoId castOther = ( ComprobantePagoId ) other; 
         
		 return ( (this.getSerieComprobante()==castOther.getSerieComprobante()) || ( this.getSerieComprobante()!=null && castOther.getSerieComprobante()!=null && this.getSerieComprobante().equals(castOther.getSerieComprobante()) ) )
 && (this.getEmpresaIdEmpresa()==castOther.getEmpresaIdEmpresa())
 && ( (this.getTipoComprobanteIdTipoComprobante()==castOther.getTipoComprobanteIdTipoComprobante()) || ( this.getTipoComprobanteIdTipoComprobante()!=null && castOther.getTipoComprobanteIdTipoComprobante()!=null && this.getTipoComprobanteIdTipoComprobante().equals(castOther.getTipoComprobanteIdTipoComprobante()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getSerieComprobante() == null ? 0 : this.getSerieComprobante().hashCode() );
         result = 37 * result + this.getEmpresaIdEmpresa();
         result = 37 * result + ( getTipoComprobanteIdTipoComprobante() == null ? 0 : this.getTipoComprobanteIdTipoComprobante().hashCode() );
         return result;
   }   


}


