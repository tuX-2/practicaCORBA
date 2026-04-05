import ChatApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

// Implementación del Skeleton generado por idlj
class SalaImpl extends SalaPOA {
    // Variable global que almacena el último mensaje recibido
    private String ultimoMensaje = "(ninguno)";

    @Override
    public void enviarMensaje(String usuario, String contenido) {
        ultimoMensaje = "[" + usuario + "]: " + contenido;
        System.out.println("Mensaje recibido → " + ultimoMensaje);
    }

    @Override
    public String leerUltimo() {
        System.out.println("Cliente solicitó leer último mensaje.");
        return ultimoMensaje;
    }
}

public class ChatServidor {
    public static void main(String[] args) {
        try {
            // 1. Inicializar el ORB con los argumentos de línea de comandos
            ORB orb = ORB.init(args, null);

            // 2. Obtener referencia al POA raíz y activarlo
            POA rootPOA = POAHelper.narrow(
                orb.resolve_initial_references("RootPOA")
            );
            rootPOA.the_POAManager().activate();

            // 3. Crear e instanciar la implementación del objeto
            SalaImpl sala = new SalaImpl();

            // 4. Obtener referencia CORBA del objeto
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(sala);
            Sala salaRef = SalaHelper.narrow(ref);

            // 5. Registrar el objeto en el Naming Service
            org.omg.CORBA.Object objRef =
                orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Nombre con el que el cliente buscará el objeto
            NameComponent path[] = ncRef.to_name("SalaChat");
            ncRef.rebind(path, salaRef);

            System.out.println("Servidor CORBA listo. Esperando clientes...");

            // 6. Esperar peticiones indefinidamente
            orb.run();

        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
