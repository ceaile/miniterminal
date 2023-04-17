
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

/**
 * Enunciado: Programa que funciona como una pequeña terminal Linux, con algunos
 * comandos (simplificados) que permitan al usuario realizar distintas
 * operaciones de gestión de archivos. Tendrá los atributos y métodos que
 * necesites, para realizar las distintas operaciones relacionadas con la
 * gestión de archivos. Necesitarás al menos un método por cada operación. Se
 * lanzará una excepción si se produce un error o la operación solicitada no es
 * posible.
 *
 * @author DAW
 */
public class MiniFileManager {

    //ATRIBUTOS
    File ruta = new File("");

    //CONSTRUCTOR
    public MiniFileManager(File ruta) throws Exception {
        if (ruta.exists()) {
            setRuta(ruta);
        } else {
            throw new ExcepcionFileManager("El constructor ha fallado. La ruta indicada puede no existir.");
        }
    }

    /**
     * Muestra cual es la carpeta actual. Devuelve una cadena de texto con la
     * carpeta actual.
     *
     * @return direccion
     * @throws Exception
     */
    public String getPWD() throws Exception {
//        return ruta.getParent() + ruta.getName();
        return ruta.getAbsolutePath();
    }

    /**
     * Cambia la carpeta actual a ‘DIR’. Con .. cambia a la carpeta superior.
     * Cambia la carpeta actual a dir. Devuelve ‘true’ si fue posible.
     *
     * @see setruta() y rutaProcesada()
     * @param dir
     * @throws Exception
     */
    public void changeDir(String dir) throws Exception {
        if (rutaProcesada(dir).exists() == false) {
            throw new ExcepcionFileManager("El cambio de ruta ha fallado. La ruta indicada puede no existir.");
        }
        setRuta(rutaProcesada(dir));
    }

    /**
     * Muestra la lista de directorios y archivos de la carpeta actual (primero
     * directorios, luego archivos, ambos ordenados alfabéticamente). Muestra
     * una lista con los directorios y archivos de la carpeta. actual. Si info
     * es ‘true’ mostrará también su tamaño y fecha de modificación. Si es
     * false: Como ls pero muestra también el tamaño y la fecha de última
     * modificación.
     *
     * @throws Exception
     */
    public void printList(boolean info) throws Exception {

        if (ruta.exists() == false) {
            throw new ExcepcionFileManager("EXCEPCION F.MANAGER: El fichero indicado no existe.");

        } else if (ruta.exists() && ruta.isFile()) {
            throw new ExcepcionFileManager("EXCEPCION F.MANAGER: Ha introducido un archivo, no un directorio.");

        } else if (ruta.exists() && ruta.isDirectory()) {
            System.out.println("Nombre del directorio: " + ruta.getName());
            File[] arrayArchivos = ruta.listFiles(); //para recordarme que es un array de Files
            ArrayList<File> listaDirs = new ArrayList<File>();
            ArrayList<File> listaFich = new ArrayList<File>();

            for (File f : arrayArchivos) {
                if (f.isDirectory()) {
                    listaDirs.add(f);
                } else {
                    listaFich.add(f);
                }
            }
            Collections.sort(listaDirs);
            Collections.sort(listaFich);

            for (File f : listaDirs) {
                System.out.println("[*]" + f.getName());
                if (info) {
                    System.out.println("Tamaño en bytes: " + f.length());
                    Date fecha = new Date(f.lastModified());
                    System.out.println("Ultima modificacion: " + fecha.toString());
                }
            }
            for (File f : listaFich) {
                System.out.println("[A]" + f.getName());
                if (info) {
                    System.out.println("Tamaño en bytes: " + f.length());
                    Date fecha = new Date(f.lastModified());
                    System.out.println("Ultima modificacion: " + fecha.toString());
                }
            }
        }
        System.out.println("Ruta madre: " + ruta.getParent());

    }

    /**
     * Crea el directorio ‘DIR’ en la carpeta actual.
     *
     * @param dir
     * @throws Exception
     */
    public void mkDir(String dir) throws Exception {
        File nuevaRuta = new File(ruta.getAbsolutePath() + "/" + dir);
        if (nuevaRuta.mkdir()) {
            System.out.println("Ruta creada con éxito.");
        } else {
            throw new ExcepcionFileManager("No ha podido crearse el directorio");
        }
    }

    /**
     * Borra ‘FILE’. Si es una carpeta, borrará primero sus archivos y luego la
     * carpeta. Si tiene subcarpetas, las dejará intactas y mostrará un aviso al
     * usuario.
     *
     * @param ruta
     * @throws Exception
     */
    public void rm(String ruta) throws Exception {

        File fichero = rutaProcesada(ruta); //new!
        if (fichero.exists()) {
            if (fichero.isFile()) {
                if (fichero.delete()) {
                    System.out.println("Archivo eliminado con éxito");
                } else {
                    throw new ExcepcionFileManager("No ha podido eliminarse el archivo especificado.");
                }
            } else if (fichero.isDirectory()) {
                File archivos[] = fichero.listFiles();
                for (int i = 0; i < archivos.length; i++) {
                    if (archivos[i].isFile() && archivos[i].delete()) {
                        System.out.println("Archivo eliminado con éxito.");
                    } else if (archivos[i].isDirectory()) {
                        System.out.println("El fichero que quieres eliminar contiene subcarpetas. No han sido eliminadas.");
                    } else {
                        throw new ExcepcionFileManager("No ha podido eliminarse algún archivo del directorio especificado.");
                    }
                }
                if (fichero.delete()) {
                    System.out.println("Directorio eliminado con éxito.");
                } else {
                    throw new ExcepcionFileManager("No ha podido eliminarse el directorio especificado.");
                }
            }
        } else {
            throw new ExcepcionFileManager("El fichero no existe. No ha podido eliminarse.");
        }

    }

    /**
     * Muestra una breve ayuda con los comandos disponibles.
     */
    public void help() {
        System.out.println("1. Escriba 'pwd' para conocer la ruta en la que se encuentra actualmente. ");
        System.out.println("2. Escriba 'cd' seguido de una ruta para cambiar la ruta actual.");
        System.out.println("3. Escriba 'ls' para conocer los archivos y directorios que se encuentran en la ruta actual.");
        System.out.println("4. Escriba 'll' para conocer los detalles de archivos y directorios");
        System.out.println("5. Escriba 'mkdir' seguido de la ruta y nombre de carpeta para crearla.");
        System.out.println("6. Escriba 'rm' seguido de la ruta de un archivo para eliminarlo.");
        System.out.println("7. Escriba 'mv' seguido de la ruta origen y la ruta destino para mover una carpeta a otro sitio.");
        System.out.println("8. Escriba 'help' para conocer los comandos disponibles.");
        System.out.println("9. Escriba 'exit' para salir del programa.");
    }

    /**
     * Renombra/mueve los archivos de una carpeta que existe ‘FILE1’ a una que
     * aún no: ‘FILE2’.
     *
     * @param ficheroOrigen
     * @param ficheroDestino
     * @throws Exception
     */
    public void move(File ficheroOrigen, File ficheroDestino) throws Exception {
        if (ficheroOrigen.exists() == true && ficheroDestino.exists() == false) {
            if (ficheroOrigen.renameTo(ficheroDestino) == false) {
                throw new ExcepcionFileManager("No ha podido moverse el fichero. Puede que no sean ficheros");
            }
        }
    }

    /**
     * Termina el programa.
     */
    public void exit() {
        System.out.println("Ha decidido salir del programa.");
    }

    public File getRuta() {
        return ruta;
    }

    private void setRuta(File ruta) {
        this.ruta = ruta;
    }

    /**
     * Compara con una expresion regular que busca "C:\" (C, u otras letras)
     * para saber si es una ruta absoluta en el sistema Windows.
     *
     * @param dir
     * @return Es ruta absoluta o relativa
     */
    static public boolean esRutaAbsoluta(String dir) {
        return dir.matches("^[a-zA-Z]:\\\\.*$");
    }

    
    /**
     * Comprueba si un String de ruta es absoluta o no,
     * comprueba si lo que se ha introducido por teclado es un ".." para retroceder,
     * y devuelve un File que ya puedes utilizar.
     * No comprueba si existe o no porque también utilizamos esta función para
     * crear ficheros y mover ficheros, los cuales no deben existir previamente.
     * @param dir
     * @return fichero
     * @throws Exception 
     */
    public File rutaProcesada(String dir) throws Exception {

        File f;

        if (esRutaAbsoluta(dir)) { //filtrar ruta absoluta
            f = new File(dir);

        } else if (dir.equalsIgnoreCase("..")) { //filtrar ..
            File fich = new File(getPWD());

            String parent = "";
            String trozos[] = fich.getAbsolutePath().split("\\\\");
            for (int i = 0; i < trozos.length - 1; i++) {
                if (!parent.equalsIgnoreCase("")) {
                    parent += "/";
                }
                parent += trozos[i];
            }
            f = new File(parent);

        } else { //ruta relativa
            f = new File(ruta + "/" + dir);
        }

//        if (f.exists() == false) {
//            throw new ExcepcionFileManager("EXCEPCION F.MANAGER: La ruta introducida no es válida");
//        }
        return f;
    }

}//fin
