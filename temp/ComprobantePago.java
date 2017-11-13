package Models;
// Generated 10-jul-2017 16:17:01 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * ComprobantePago generated by hbm2java
 */
public class ComprobantePago  implements java.io.Serializable {


     private ComprobantePagoId id;
     private float saldoBruto;
     private float impuesto;
     private float total;
     private String moneda;
     private Date fechaEmision;
     private String observacion;
     private String estado;
     private Float tipoCambioCompra;
     private Float tipoCambioVenta;
     private Date fechaRegistro;
     private String clienteNumero;
     private int cuentaIdCuenta;
     private String serie;
     private Integer empresaIdEmpresa;
     private String numGuia;
     private String serieGuia;

    public ComprobantePago() {
    }

	
    public ComprobantePago(ComprobantePagoId id, float saldoBruto, float impuesto, float total, String moneda, Date fechaEmision, String estado, String clienteNumero, int cuentaIdCuenta) {
        this.id = id;
        this.saldoBruto = saldoBruto;
        this.impuesto = impuesto;
        this.total = total;
        this.moneda = moneda;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.clienteNumero = clienteNumero;
        this.cuentaIdCuenta = cuentaIdCuenta;
    }
    public ComprobantePago(ComprobantePagoId id, float saldoBruto, float impuesto, float total, String moneda, Date fechaEmision, String observacion, String estado, Float tipoCambioCompra, Float tipoCambioVenta, Date fechaRegistro, String clienteNumero, int cuentaIdCuenta) {
       this.id = id;
       this.saldoBruto = saldoBruto;
       this.impuesto = impuesto;
       this.total = total;
       this.moneda = moneda;
       this.fechaEmision = fechaEmision;
       this.observacion = observacion;
       this.estado = estado;
       this.tipoCambioCompra = tipoCambioCompra;
       this.tipoCambioVenta = tipoCambioVenta;
       this.fechaRegistro = fechaRegistro;
       this.clienteNumero = clienteNumero;
       this.cuentaIdCuenta = cuentaIdCuenta;
    }

    public String getNumGuia() {
        return numGuia;
    }

    public void setNumGuia(String numGuia) {
        this.numGuia = numGuia;
    }

    public String getSerieGuia() {
        return serieGuia;
    }

    public void setSerieGuia(String serieGuia) {
        this.serieGuia = serieGuia;
    }
    
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getEmpresaIdEmpresa() {
        return empresaIdEmpresa;
    }

    public void setEmpresaIdEmpresa(Integer empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }
   
    public ComprobantePagoId getId() {
        return this.id;
    }
    
    public void setId(ComprobantePagoId id) {
        this.id = id;
    }
    public float getSaldoBruto() {
        return this.saldoBruto;
    }
    
    public void setSaldoBruto(float saldoBruto) {
        this.saldoBruto = saldoBruto;
    }
    public float getImpuesto() {
        return this.impuesto;
    }
    
    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }
    public float getTotal() {
        return this.total;
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
    public String getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public Date getFechaEmision() {
        return this.fechaEmision;
    }
    
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public String getObservacion() {
        return this.observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Float getTipoCambioCompra() {
        return this.tipoCambioCompra;
    }
    
    public void setTipoCambioCompra(Float tipoCambioCompra) {
        this.tipoCambioCompra = tipoCambioCompra;
    }
    public Float getTipoCambioVenta() {
        return this.tipoCambioVenta;
    }
    
    public void setTipoCambioVenta(Float tipoCambioVenta) {
        this.tipoCambioVenta = tipoCambioVenta;
    }
    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    public String getClienteNumero() {
        return this.clienteNumero;
    }
    
    public void setClienteNumero(String clienteNumero) {
        this.clienteNumero = clienteNumero;
    }
    public int getCuentaIdCuenta() {
        return this.cuentaIdCuenta;
    }
    
    public void setCuentaIdCuenta(int cuentaIdCuenta) {
        this.cuentaIdCuenta = cuentaIdCuenta;
    }




}


