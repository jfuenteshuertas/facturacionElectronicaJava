package Models;
// Generated 10-jul-2017 16:17:01 by Hibernate Tools 4.3.1



/**
 * Producto generated by hbm2java
 */
public class Producto  implements java.io.Serializable {


     private Integer idProducto;
     private String descripcion;
     private float precioUnit;
     private String unidadMedida;
     private String moneda;
     private int empresaIdEmpresa;
     private float cantidad;
     private float impuesto;

    public Producto() {
    }

	
    public Producto(String descripcion, float precioUnit, String moneda, int empresaIdEmpresa) {
        this.descripcion = descripcion;
        this.precioUnit = precioUnit;
        this.moneda = moneda;
        this.empresaIdEmpresa = empresaIdEmpresa;
    }
    public Producto(String descripcion, float precioUnit, String unidadMedida, String moneda, int empresaIdEmpresa) {
       this.descripcion = descripcion;
       this.precioUnit = precioUnit;
       this.unidadMedida = unidadMedida;
       this.moneda = moneda;
       this.empresaIdEmpresa = empresaIdEmpresa;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }
    
    public Integer getIdProducto() {
        return this.idProducto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }
    
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public float getPrecioUnit() {
        return this.precioUnit;
    }
    
    public void setPrecioUnit(float precioUnit) {
        this.precioUnit = precioUnit;
    }
    public String getUnidadMedida() {
        return this.unidadMedida;
    }
    
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    public String getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public int getEmpresaIdEmpresa() {
        return this.empresaIdEmpresa;
    }
    
    public void setEmpresaIdEmpresa(int empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }




}

