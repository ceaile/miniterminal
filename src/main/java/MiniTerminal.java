
import java.io.File;
import java.util.Scanner;

/* Enunciado:
Implementa un programa que funcione como una pequeña terminal Linux, con algunos comandos
(simplificados) que permitan al usuario realizar distintas operaciones de gestión de archivos. Los
comandos que el usuario podrá ejecutar son:
● pwd: Muestra cual es la carpeta actual.
● cd <DIR>: Cambia la carpeta actual a ‘DIR’. Con .. cambia a la carpeta superior.
● ls: Muestra la lista de directorios y archivos de la carpeta actual (primero directorios, luego
archivos, ambos ordenados alfabéticamente).
● ll: Como ls pero muestra también el tamaño y la fecha de última modificación.
● mkdir <DIR>: Crea el directorio ‘DIR’ en la carpeta actual.
● rm <FILE>: Borra ‘FILE’. Si es una carpeta, borrará primero sus archivos y luego la carpeta. Si
tiene subcarpetas, las dejará intactas y mostrará un aviso al usuario.
● mv <FILE1> <FILE2>: Mueve o renombra ‘FILE1’ a ‘FILE2’.
● help: Muestra una breve ayuda con los comandos disponibles.
● exit: Termina el programa.


 */
/**
 * Clase principal (con función main) que se encargará de interactuar con el
 * usuario e interpretar los comandos (qué comando se pide, argumentos, etc.).
 * Utilizará la segunda clase para realizar las operaciones de gestión de
 * archivos. Manejará todas las posibles excepciones.
 *
 * @author DAW
 */
public class MiniTerminal {

    /**
     * MAIN
     *
     * @param args
     */
    public static void main(String[] args) {//main

        Scanner teclado = new Scanner(System.in);
        String opcionElegida = "";

        File ruta = new File("Documentos");
        MiniFileManager linux = null;
        try {
            linux = new MiniFileManager(ruta);
        } catch (Exception e) {
            System.out.println("EXCEPCION: Algo ha salido mal :( ");
        }

        System.out.println("Bienvenido al MiniTerminal Linux.");
        System.out.println("Escriba 'help' para saber sus opciones.");
        do {
            try {

                System.out.println();
                System.out.print(" $ ");
                opcionElegida = teclado.nextLine();

                //PWD
                if (opcionElegida.equalsIgnoreCase("pwd")) {
                    System.out.println("Se muestra la ruta del directorio actual");
                    System.out.println(linux.getPWD());
                    System.out.println();

                    //CD DIRECTORY
                } else if (opcionElegida.matches("^(?i)cd .*$")) {
                    System.out.println("Se cambia la ruta actual por la indicada.");
                    String trozosComandoRuta[] = opcionElegida.split(" ");
                    if (trozosComandoRuta.length > 2 || trozosComandoRuta.length <= 1) {
                        throw new ExcepcionTerminal("EXCEPCION TERMINAL: Debe indicar solo una ruta");
                    } else {
                        linux.changeDir(trozosComandoRuta[1]);
                    }

                    //LS
                } else if (opcionElegida.equalsIgnoreCase("ls")) {
                    System.out.println("Se muestran los archivos y carpetas de la ruta actual.");
                    linux.printList(false);

                    //LL
                } else if (opcionElegida.equalsIgnoreCase("ll")) {
                    System.out.println("Se muestran los archivos, carpetas e informacion adicional de la ruta actual.");
                    linux.printList(true);

                    //MKDIR    
                } else if (opcionElegida.matches("^(?i)mkdir .*$")) {
                    System.out.println("Se crea el directorio de la ruta indicada");
                    String trozosComandoRuta[] = opcionElegida.split(" ");
                    if (trozosComandoRuta.length > 2 || trozosComandoRuta.length <= 1) {
                        throw new ExcepcionTerminal("EXCEPCION TERMINAL: Debe indicar solo una ruta");
                    } else {
                        linux.mkDir(trozosComandoRuta[1]); //incluye mensaje de creacion con exito o error
                    }

                    //RM (REMOVE DIRECTORY)
                } else if (opcionElegida.matches("^(?i)rm .*$")) {
                    System.out.println("Se elimina el directorio indicado");
                    String trozosComandoRuta[] = opcionElegida.split(" ");
                    if (trozosComandoRuta.length > 2 || trozosComandoRuta.length <= 1) {
                        throw new ExcepcionTerminal("EXCEPCION TERMINAL: Debe indicar solo una ruta");
                    } else {
//                        File ficheroBorrar = new File(trozosComandoRuta[1]);
                        linux.rm(trozosComandoRuta[1]);

                    }

                    //MV (MOVER)
                } else if (opcionElegida.matches("^(?i)mv .*$")) {
                    System.out.println("Se mueven los archivos de la ruta indicada origen a la ruta indicada destino");
                    String trozosComandoRuta[] = opcionElegida.split(" ");
                    if (trozosComandoRuta.length > 3 || trozosComandoRuta.length <= 2) {
                        throw new ExcepcionTerminal("EXCEPCION TERMINAL: Debe indicar 2 rutas, origen y destino.");
                    } else {
                        File archivo1 = linux.rutaProcesada(trozosComandoRuta[1]);
                        File archivo2 = linux.rutaProcesada(trozosComandoRuta[2]);
                        linux.move(archivo1, archivo2);
                    }

                    //HELP
                } else if (opcionElegida.equalsIgnoreCase("help")) {
                    System.out.println("Se muestran por pantalla todas las opciones de los usuarios.");
                    linux.help();

                } else if (opcionElegida.equalsIgnoreCase("exit")) {
                    linux.exit();
                    System.out.println("Se sale del programa.");
                } else {
                    System.out.println("Ha introducido un comando inválido.");
                }
            } catch (ExcepcionTerminal et) {
//                System.out.println(et.getMessage());
                et.printStackTrace();
            } catch (ExcepcionFileManager efm) {
                efm.printStackTrace();
            } catch (Exception e) {
                /* nota para mí misma:
                la excepcion generica se va a usar para capturar las excepciones que no hayamos
                tenido en cuenta. Por eso, es mejor que tenga un mensaje aquí en lugar de 
                sacar el getMessage(), que sacaria un chorizo.
                 */
                e.printStackTrace();
                System.out.println("EXCEPCION: Algo ha salido mal :( ");
            }
        } while (!opcionElegida.equalsIgnoreCase("exit"));

    }//fin main   

}//fin
