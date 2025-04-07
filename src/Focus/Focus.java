package src.Focus;

import src.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Focus {
    // id del foco:
    public int id_focus;

    // nombre del foco
    public String name_focus;

    // pila de focos de la ventana principal
    public static HashMap<String, Focus> stack_focus_root = new HashMap<>();
    public static Focus now_Focus = null; // Foco actual selecionado

    // pila de focos del foco actual (local)
    public HashMap<String, Focus> stack_focus_local;

    // información sobre el foco
    public InformationFocus information;

    public GUI gui;

    public static String getLastNameFocus(String[] string) {
        return string[string.length - 1];
    }

    private void print_HashMap(HashMap<String, Focus> map, int iteration) {
        for (Map.Entry<String, Focus> entry : map.entrySet()) {
            System.out.println("%sname: %s id: %d".formatted(
                    "\t".repeat(iteration), entry.getKey(), 
                    entry.getValue().id_focus)
                );
            if (entry.getValue().stack_focus_local != null) {
                print_HashMap(
                    entry.getValue().stack_focus_local, 
                    iteration + 1
                );
            }
        }
    }


    /*
     * obtiene el Foco padre de un foco hijo
     * root = ruta con el nombre del foco hijo
     *
     */
    public static Focus get_FocusFather(
        HashMap<String, Focus> map, String[] arr_ids
        ) {
        // obtener el nombre del ultimo Foco
        Focus current = map.get(arr_ids[0]); // empieza en "root"
        for (int i = 1; i < arr_ids.length - 1; i++) {  // recorre hasta el penúltimo elemento
            if (current != null) {
                current = current.stack_focus_local.get(arr_ids[i]);
            }
        }
        return current;
    }

    public static void deleteFocusInStack(String root) {
        /*
         * elimina un foco/subfoco
         */
        String[] arr_ids = root.split("\\.");
        Focus subfoco = get_FocusFather(stack_focus_root, arr_ids);
        subfoco.stack_focus_local.remove(getLastNameFocus(arr_ids));
    }

    public Focus(GUI gui,
        int id_focus,
        String name_focus,
        HashMap<String, Focus> stack_focus_local
        ) {
        this.gui = gui;
        this.id_focus = id_focus;
        this.name_focus = name_focus;
        this.stack_focus_local = stack_focus_local;
        this.information = new InformationFocus(gui,
            0, 0, 1, 1, Color.BLUE);//GUI.dotDrawer.getBackground());
    }
    public void render(Graphics2D g, BufferedImage guiBuffer) {
        if (information != null) {
            information.paint();  // Asegurar que la información del foco se dibuja
            information.render(g, guiBuffer);
        }

        // Renderizar subfocos
        for (Focus subFocus : stack_focus_local.values()) {
            subFocus.render(g, guiBuffer);
        }
    }


    // Método para añadir subfocos
    public void addSubFocus(Focus child) {
        if (child != null) {
            stack_focus_local.put(child.name_focus, child);
        }
    }
    public Focus(GUI gui, int id_focus, String name_focus) {
        this.gui = gui;
        this.id_focus = id_focus;
        this.name_focus = name_focus;
        this.information = new InformationFocus(gui, 0, 0, 1, 1, Color.BLUE);

        String[] arr_ids = name_focus.split("\\.");
        // Reasignar name_focus al último token (por ejemplo, "a")
        this.name_focus = arr_ids[arr_ids.length - 1];
        this.id_focus = id_focus;
        this.stack_focus_local = new HashMap<>();
        this.information = new InformationFocus(gui,0, 0, 1, 1, Color.BLUE);//GUI.dotDrawer.getBackground());

        // El primer token debe ser "root"
        if (!arr_ids[0].equalsIgnoreCase("root")) {
            throw new IllegalArgumentException("El nombre del foco debe iniciar con 'root'");
        }

        // Obtener o crear el foco raíz en stack_focus_root
        Focus root = stack_focus_root.get("root");
        if (root == null) {
            if (arr_ids.length == 1) {
                this.information.width = gui.frame.getWidth();
                this.information.height = gui.frame.getHeight();
                stack_focus_root.put("root", this);
                root = this;
            } else {
                root = new Focus(gui,0, "root", new HashMap<String, Focus>());
                root.information.width = gui.frame.getWidth();
                root.information.height = gui.frame.getHeight();
                root.information.color = Color.BLACK;
                stack_focus_root.put("root", root);
            }
        }

        // Navegar o crear la jerarquía para los subfocos
        Focus parent = root;
        for (int i = 1; i < arr_ids.length; i++) {
            String token = arr_ids[i];
            Focus child = parent.stack_focus_local.get(token);
            if (child == null) {
                if (i == arr_ids.length - 1) {
                    // En el último token, se inserta el foco actual
                    parent.stack_focus_local.put(token, this);
                    parent = this; // El foco actual es el final de la cadena
                } else {
                    // Crear un subfoco intermedio
                    child = new Focus(gui, 0, token, new HashMap<String, Focus>());
                    parent.stack_focus_local.put(token, child);
                    parent = child;
                }
            } else {
                // Si ya existe, avanzar a ese nodo
                parent = child;
            }
        }

        now_Focus = this; // Indicar que este es el foco actual
        print_HashMap(stack_focus_root, 0);
    }

    @Override
    public String toString() {
        return "%s:%d".formatted(name_focus, id_focus);
    }

    public void paint(Focus focus){
    }
}
