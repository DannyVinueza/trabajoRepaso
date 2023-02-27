import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class aplicacion {
    PreparedStatement ps;
    private JPanel pantalla;
    private JTextField id;
    private JTextField descripcion;
    private JTextField cantidad;
    private JTextField precio;
    private JComboBox categoria;
    private JTextField fechasis;
    private JTextField fechaing;
    private JButton eliminarButton;
    private JButton buscarButton;
    private JButton modificar;
    private JButton crear;
    private JButton limpiarButton;

    public aplicacion() {
        Connection con;
        try{
            con = getConection();
            String query = "SELECT * FROM categorias";
            Statement s = con.createStatement();
            ResultSet rs =s.executeQuery(query);
            while(rs.next()){
                categoria.addItem(rs.getString(2));
            }
        }catch (HeadlessException | SQLException e){
            System.out.println(e);
        }


        limpiarButton.addActionListener(new ActionListener() {//Boton de limpiar
            @Override
            public void actionPerformed(ActionEvent e) {
                id.setText("");
                descripcion.setText("");
                cantidad.setText("");
                precio.setText("");
                fechasis.setText("");
                fechaing.setText("");
            }
        });
        crear.addActionListener(new ActionListener() {//Boton crear
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try{
                    con = getConection();
                    ps = con.prepareStatement("INSERT INTO stock (descripcion, cantidad, precio, categoria, fechaSis, fechaIng) VALUES (?,?,?,?,?,?)");
                    ps.setString(1, descripcion.getText());
                    ps.setInt(2, Integer.parseInt(cantidad.getText()));
                    ps.setBigDecimal(3, new BigDecimal(precio.getText()));
                    ps.setString(4, (String) categoria.getSelectedItem());
                    ps.setDate(5, new Date(System.currentTimeMillis()));
                    ps.setDate(6, Date.valueOf(LocalDate.parse(fechaing.getText())));
                    System.out.println(ps);

                    int cont = ps.executeUpdate();
                    if(cont > 0){
                        JOptionPane.showMessageDialog(null, "Registro guardado satisfactoriamente");
                    }else{
                        JOptionPane.showMessageDialog(null, "Error no se pudo guardar");
                    }
                    con.close();
                }catch (SQLException s){
                    System.out.println("Error " + s.getMessage());
                }
            }
        });
        modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try{

                    con = getConection();
                    // Consulta para verificar si el registro existe
                    String existeId = "SELECT COUNT(*) FROM stock WHERE idstock = ?";
                    PreparedStatement existeIDp = con.prepareStatement(existeId);
                    existeIDp.setString(1, id.getText());
                    ResultSet existeIDPR = existeIDp.executeQuery();
                    existeIDPR.next();
                    int count = existeIDPR.getInt(1);

                    if(count > 0){
                        ps = con.prepareStatement("UPDATE stock SET descripcion = ?, cantidad = ?, precio = ?, categoria = ?, fechaSis = ?, fechaIng = ? WHERE idstock = " + id.getText());
                        ps.setString(1, descripcion.getText());
                        ps.setInt(2, Integer.parseInt(cantidad.getText()));
                        ps.setBigDecimal(3,new BigDecimal(precio.getText()));
                        ps.setString(4, (String)categoria.getSelectedItem());
                        ps.setDate(5, new Date(System.currentTimeMillis()));
                        ps.setDate(6, Date.valueOf(LocalDate.parse(fechaing.getText())));
                        System.out.println(ps);

                        int cont = ps.executeUpdate();
                        if(cont > 0){
                            JOptionPane.showMessageDialog(null,"Actualizacion exitosa");
                        }else{
                            JOptionPane.showMessageDialog(null,"Error al actulizar");
                        }
                        con.close();
                    }else{
                        JOptionPane.showMessageDialog(null,"No existe el id del stoxk a modificar");
                    }

                }catch(SQLException s){
                    System.out.println(s);
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try{
                    con = getConection();
                    String consulta = "SELECT * FROM stock WHERE idstock = " + id.getText();
                    Statement s =con.createStatement();
                    ResultSet rs = s.executeQuery(consulta);

                    if(rs.next()){
                        if(id.getText().equals(rs.getString(1))){
                            descripcion.setText(rs.getString(2));
                            cantidad.setText(rs.getString(3));
                            precio.setText(rs.getString(4));
                            categoria.setSelectedItem(rs.getString(5));
                            fechasis.setText(rs.getString(6));
                            fechaing.setText(rs.getString(7));
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Producto no encontrado");
                    }

                }catch (SQLException s){
                    System.out.println(s);
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try{
                    con = getConection();
                    String conElim = "SELECT COUNT(*) FROM stock WHERE idstock = ?";
                    PreparedStatement existeD = con.prepareStatement(conElim);
                    existeD.setString(1, id.getText());
                    ResultSet rsD = existeD.executeQuery();
                    rsD.next();
                    int counD = rsD.getInt(1);

                    if(counD > 0){
                        ps = con.prepareStatement("DELETE FROM stock WHERE idstock = " + id.getText());
                        int cont =ps.executeUpdate();

                        if(cont > 0){
                            JOptionPane.showMessageDialog(null, "Producto Eliminado");
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al eliminar el producto");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"No se ecunetra el id para eliminar");
                    }
                }catch (SQLException s){
                    System.out.println(s);
                }
            }
        });
        categoria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object seleccion = categoria.getSelectedItem();
                Boolean mensajeV = true;
                if(seleccion.equals("Frutas")){
                    JOptionPane.showMessageDialog(null,"A escogido Frutas");
                } else if (seleccion.equals("Verduras")) {
                    JOptionPane.showMessageDialog(null, "A escogido Verduras");
                } else if (seleccion.equals("Legumbres")) {
                    JOptionPane.showMessageDialog(null, "A escogido Legumbres");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Recuperacion");
        frame.setContentPane(new aplicacion().pantalla);
        frame.setSize(1700,1000);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Connection getConection(){
        Connection con = null;
        String nombreBD = "pruebare";
        String url = "jdbc:mysql://localhost:3306/" + nombreBD;
        String user = "root";
        String password = "Danny.2002";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,password);
            System.out.println("Conexion Exitosa");
        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
            System.out.println("Eroor en la conexion..");
        }
        return con;
    }

}

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Obtener los valores del ID del producto y la cantidad a disminuir
int idProducto = 123;
int cantidadDisminuir = 5;

// Establecer la conexión a la base de datos
String url = "jdbc:mysql://localhost:3306/mi_base_de_datos";
String usuario = "mi_usuario";
String contrasena = "mi_contrasena";
Connection conexion = DriverManager.getConnection(url, usuario, contrasena);

// Disminuir la cantidad de productos en la tabla "productos"
String sqlProductos = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";
PreparedStatement declaracionProductos = conexion.prepareStatement(sqlProductos);
declaracionProductos.setInt(1, cantidadDisminuir);
declaracionProductos.setInt(2, idProducto);
declaracionProductos.executeUpdate();

// Actualizar la cantidad de productos en la tabla "stock"
String sqlStock = "UPDATE stock SET cantidad = cantidad - ? WHERE id_producto = ?";
PreparedStatement declaracionStock = conexion.prepareStatement(sqlStock);
declaracionStock.setInt(1, cantidadDisminuir);
declaracionStock.setInt(2, idProducto);
declaracionStock.executeUpdate();

// Cerrar la conexión a la base de datos
conexion.close();
*/
