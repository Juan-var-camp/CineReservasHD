package model;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private String tipo;

    public Usuario() {}
    
    public Usuario(int id, String username, String password, String nombre) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
    }

    public String getUsername() { return username;    }

    public String getPassword() { return password;    }

    public String getNombre() { return nombre;    }

    public void setUsername(String username) { this.username = username;    }

    public void setPassword(String password) { this.password = password;    }

    public void setNombre(String nombre) { this.nombre = nombre;    }

    public String getTipo() { return tipo;    }

    public void setTipo(String tipo) { this.tipo = tipo;    }

    public void setId(int id) { this.id = id;    }

    public int getId() { return id;    }
}