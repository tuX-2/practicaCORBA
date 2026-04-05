import ChatApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class ChatCliente {
    public static void main(String[] args) throws Exception {
        ORB orb = ORB.init(args, null);

        org.omg.CORBA.Object objRef =
            orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        Sala sala = SalaHelper.narrow(ncRef.resolve_str("SalaChat"));

        java.util.Scanner sc = new java.util.Scanner(System.in);
        while (true) {
            System.out.println("\n1. Enviar  2. Leer  3. Salir");
            String op = sc.nextLine().trim();
            if (op.equals("1")) {
                System.out.print("Usuario: "); String u = sc.nextLine();
                System.out.print("Mensaje: "); String m = sc.nextLine();
                sala.enviarMensaje(u, m);
                System.out.println("✓ Enviado.");
            } else if (op.equals("2")) {
                System.out.println("Último: " + sala.leerUltimo());
            } else if (op.equals("3")) {
                break;
            }
        }
        orb.destroy();
    }
}
