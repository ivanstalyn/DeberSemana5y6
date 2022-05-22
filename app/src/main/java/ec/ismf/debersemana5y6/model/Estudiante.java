package ec.ismf.debersemana5y6.model;

public class Estudiante {
    String codigo;
    String nombre;
    String apellido;
    String edad;

    public Estudiante(){

    }

    public Estudiante(String codigo, String nombre, String apellido, int edad){

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
