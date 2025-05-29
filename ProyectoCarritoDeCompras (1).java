package com.poo2025.proyectocarritodecompras;

/*
Programa Realizado por:
Estrella AA
Yael LA
Brian Joel MJ
Yael VV

Ingenieria en TICS | POO 
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Producto {

    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }

    @Override
    public String toString() {
        return nombre + " x" + cantidad + " ($" + precio + ") - Subtotal: $" + getSubtotal();
    }
}

class CarritoCompras {

    private ArrayList<Producto> productos = new ArrayList<>();
    private double descuento = 1.0;
    private boolean descuentoAplicado = false;
    private String mensajeDescuento = "";

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(int indice) {
        if (indice >= 0 && indice < productos.size()) {
            productos.remove(indice);
        }
    }

    public void reiniciarCarrito() {
        productos.clear();
        descuento = 1.0;
        descuentoAplicado = false;
        mensajeDescuento = "";
    }

    public void aplicarDescuento(String codigo) {
        if (!descuentoAplicado) {
            if (codigo.equals("123")) {
                descuento = 0.8;
                mensajeDescuento = "Descuento del 20% aplicado.";
                descuentoAplicado = true;
            } else if (codigo.equals("456")) {
                descuento = 0.5;
                mensajeDescuento = "Descuento del 50% aplicado.";
                descuentoAplicado = true;
            } else {
                mensajeDescuento = "Código no válido.";
            }
        } else {
            mensajeDescuento = "Ya se ha aplicado un descuento.";
        }
    }

    public double calcularTotal() {
        double total = productos.stream().mapToDouble(Producto::getSubtotal).sum();
        return total * descuento;
    }

    public String obtenerResumen() {
        StringBuilder resumen = new StringBuilder();
        for (int i = 0; i < productos.size(); i++) {
            resumen.append(i + 1).append(". ").append(productos.get(i)).append("\n");
        }
        resumen.append("\n").append(mensajeDescuento).append("\nTotal: $").append(calcularTotal());
        return resumen.toString();
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }
}

public class ProyectoCarritoDeCompras extends JFrame {

    private CarritoCompras carrito = new CarritoCompras();
    private JTextArea areaResumen;
    private JTextField txtNombre, txtPrecio, txtCantidad, txtCodigo;
    private JLabel lblDescuento;

    public ProyectoCarritoDeCompras() {
        setTitle("Proyecto de carrito de compras - BYYE");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(230, 240, 255));

        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setBackground(new Color(220, 230, 250));
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new GridLayout(5, 2));
        panelEntrada.setBackground(new Color(200, 220, 255));

        panelEntrada.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelEntrada.add(txtNombre);

        panelEntrada.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelEntrada.add(txtPrecio);

        panelEntrada.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelEntrada.add(txtCantidad);

        panelEntrada.add(new JLabel("Código de Descuento:"));
        txtCodigo = new JTextField();
        panelEntrada.add(txtCodigo);

        JButton btnAplicarDescuento = new JButton("Aplicar Descuento");
        btnAplicarDescuento.setBackground(new Color(100, 180, 255));
        btnAplicarDescuento.setForeground(Color.BLACK);
        lblDescuento = new JLabel("");

        btnAplicarDescuento.addActionListener(e -> {
            carrito.aplicarDescuento(txtCodigo.getText());

            String[] lineasResumen = carrito.obtenerResumen().split("\n");
            if (lineasResumen.length > 2) {
                lblDescuento.setText(lineasResumen[lineasResumen.length - 2]);
            } else {
                lblDescuento.setText("No hay descuento aplicado.");
            }

            actualizarResumen();
        });

        panelEntrada.add(btnAplicarDescuento);
        panelEntrada.add(lblDescuento);

        add(panelEntrada, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 10, 10));
        panelBotones.setBackground(new Color(180, 200, 255));

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(120, 200, 120));
        btnAgregar.setForeground(Color.BLACK);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(255, 100, 100));
        btnEliminar.setForeground(Color.BLACK);

        JButton btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBackground(new Color(255, 180, 100));
        btnReiniciar.setForeground(Color.BLACK);

        JButton btnPagar = new JButton("Pagar Ahora");
        btnPagar.setBackground(new Color(150, 150, 255));
        btnPagar.setForeground(Color.BLACK);

        //Botón para agregar un artículo
        btnAgregar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío.");
                    return;
                }

                carrito.agregarProducto(new Producto(nombre, precio, cantidad));
                txtNombre.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");

                actualizarResumen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese valores válidos para precio y cantidad.");
            }
        });

        //Botón para eliminar un artículo del carrito
        btnEliminar.addActionListener(e -> {
            if (carrito.estaVacio()) {
                JOptionPane.showMessageDialog(this, "No hay productos en el carrito para eliminar.");
                return;
            }

            String indiceStr = JOptionPane.showInputDialog("Ingrese el número de producto a eliminar:");
            try {
                int indice = Integer.parseInt(indiceStr) - 1;
                carrito.eliminarProducto(indice);
                actualizarResumen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número inválido.");
            }
        });

        //Botón de reiniciar carrito
        btnReiniciar.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres reiniciar el carrito?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                carrito.reiniciarCarrito();
                txtNombre.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
                txtCodigo.setText("");
                lblDescuento.setText("");
                actualizarResumen();
            }
        });

        btnPagar.addActionListener(e -> {
            if (!carrito.estaVacio()) {
                JOptionPane.showMessageDialog(this, "Gracias por su compra");

                // Vaciar el carrito
                carrito.reiniciarCarrito();

                // Limpiar los campos de entrada
                txtNombre.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
                txtCodigo.setText("");

                // Borrar mensaje de descuento
                lblDescuento.setText("");

                // Actualizar la interfaz
                actualizarResumen();
            } else {
                JOptionPane.showMessageDialog(this, "El carrito está vacío. Agrega productos antes de pagar.");
            }
        });

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReiniciar);
        panelBotones.add(btnPagar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarResumen() {
        areaResumen.setText(carrito.obtenerResumen());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyectoCarritoDeCompras().setVisible(true));
    }

}
