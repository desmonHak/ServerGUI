package src.Focus;

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

    static String getLastNameFocus(String[] string) {
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

    public Focus(
        int id_focus, 
        String name_focus, 
        HashMap<String, Focus> stack_focus_local
        ) {
        this.id_focus = id_focus;
        this.name_focus = name_focus;
        this.stack_focus_local = stack_focus_local;
        this.information = new InformationFocus(
            0, 0, 50, 50, Color.BLUE);//GUI.dotDrawer.getBackground());
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
    
    public Focus(int id_focus, String name_focus) {
        this.id_focus = id_focus;
        this.name_focus = name_focus;
        this.stack_focus_local = new HashMap<>();
        this.information = new InformationFocus(
            0, 0, 50, 50, Color.BLUE);//GUI.dotDrawer.getBackground());

        // root.foco1.subfoco1
        String[] arr_ids = this.name_focus.split("\\.");
        HashMap<String, Focus> focus_instance = stack_focus_root;
        Focus sub_focus = null;
        for (String name : arr_ids) {
            // si es la ventana padre
            if (this.name_focus.equalsIgnoreCase("root")) {
                /*if (arr_ids.length == 1) {
                    stack_focus_root.put(this.name_focus, this);
                }*/
                /*si arr_ids no es 1(solo es la palabra root), entonces 
                no agregar a la ventana padre y saltarse este id */ 
            } else {
                // obtener el subfoco por su nombre
                sub_focus = focus_instance.get(name);   
                if (sub_focus == null) {
                    // si el subfoco no existe, crearlo
                    focus_instance.put(name, new Focus(
                        0, name, new HashMap<String, Focus>()));
                    sub_focus = focus_instance.get(name);
                }
                // cambiar la variable para seguir iterando las rutas
                focus_instance = sub_focus.stack_focus_local; 
            }
        }

        /*if (arr_ids.length != 1) {
            if (sub_focus != null) {
                // obtener el nombre real del foco
                this.name_focus = getLastNameFocus(arr_ids);
                focus_instance.put(this.name_focus, this);
            }
        }*/
        now_Focus = this; // indicar que el foco actual es el nuevo foco
        print_HashMap(stack_focus_root, 0);

    }

    @Override
    public String toString() {
        return "%s:%d".formatted(name_focus, id_focus);
    }

    public void paint(Focus focus){
    }
}
