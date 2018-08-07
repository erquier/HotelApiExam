package com.mycompany.hotelapi;

import com.google.gson.Gson;
import static com.mycompany.hotelapi.Main.userUsernameMap;
import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static java.rmi.server.LogStream.log;
import java.util.ArrayList;
import java.util.Date;
import static spark.Spark.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import spark.ModelAndView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import spark.Filter;
import spark.Spark;
import java.util.*;
import java.text.*;
import java.util.concurrent.ConcurrentHashMap;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

public class Main {

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    static Map<org.eclipse.jetty.websocket.api.Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Used for creating the next username

    private static final String SESSION_NAME = "username";
    private static SessionFactory factory;

    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    protected void exit() {
        factory.close();
    }

    public static void main(String[] args) {

        staticFiles.location("/public");

        webSocket("/chat", ChatWebSocketHandler.class);
        init();
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");

        });

        before("/protected/*", (request, response) -> {
            if (request.session(true).attribute("user") == null) {
                halt(401, "Go Away!");
            }
        });

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST");

        });

        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        //Login
//        post("/login", (request, response) -> {
//            String user = request.queryParams("user");
//            String pass = request.queryParams("pass");
//            if (user.toLowerCase().equals("admin") && pass.equals("123456")) {
//                request.session().attribute(SESSION_NAME, user);
//                response.redirect("/");
//            } else {
//                response.redirect("/?err=1");
//            }
//            return null;
//        });
        //LOGIN---------------------------------------------------------------------------------------------
        get("/login", (req, res) -> {

            String username = req.session().attribute(SESSION_NAME);
            String nombre = req.session().attribute(SESSION_NAME);
            String password = req.session().attribute(SESSION_NAME);
            String email = req.session().attribute(SESSION_NAME);
            String genero = req.session().attribute(SESSION_NAME);
            if (username == null) {

                return "<html><body>login please: <form action=\"/login\" method=\"POST\">"
                        + "<input type=\"text\" username=\"username\" value=\"Username\"/><br>"
                        + "<input type=\"text\" password=\"password\"/><br>"
                        + "<input type=\"text\" nombre=\"nombre\"/><br>"
                        + "<input type=\"text\" email=\"email\"/><br>"
                        + "<input type=\"text\" genero=\"genero\"/><br>"
                        + "<br><br><input type=\"submit\" value=\"go\"/>"
                        + "</form></body></html>";
            } else {

                return String.format("<html><body>Hello, %s!</body></html>", username);
            }

        });

//        post("/login", (req, res) -> {
//            String username = req.queryParams("username");
//            String nombre = req.queryParams("nombre");
//            String password = req.queryParams("password");
//            String email = req.queryParams("email");
//            String genero = req.queryParams("genero");
//
//            ArrayList<Usuario> lista = req.session().attribute("username");
//
//            if (lista == null) {
//                return "list equals null , you can't Log in";
//            }
//
//            boolean inicioSesion = false;
//            for (Usuario u : lista) {
//
//                if (u.getNombre() == nombre) {
//                    req.session().attribute("usuarioIniciado", u);
//                    inicioSesion = true;
//                }
//            }
//            if (inicioSesion) {
//                return "Loged in succesfully";
//            }
//
//            return "not loged, need to log in";
//        });//FIN INICIO SESION
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String nombre = req.queryParams("nombre");
            String password = req.queryParams("password");
            String email = req.queryParams("email");
            String genero = req.queryParams("genero");

            String insertID = "";
            Session session = factory.openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                Usuario user = new Usuario(username, nombre, password, email, genero);
                insertID = session.save(user).toString();
                tx.commit();

            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
            return "Agregado" + insertID;

        });//FIN LOGIN--------------------------------------------------------------------------------------

        //CLIENTE--------------------------------------------------------------------------------------------
        get("/cliente", (req, res) -> {
            res.redirect("createclient.html");

            return null;
        });

        post("/cliente", (req, res) -> {
            System.out.println("nombre" + req.queryParams("nombre"));
            String nombre = req.queryParams("nombre");
            String email = req.queryParams("email");
            String password = req.queryParams("password");
            String direccion = req.queryParams("direccion");
            String telefono = req.queryParams("telefono");

            int estado = Integer.parseInt(req.queryParams("estado"));

            String insertID = "";
            Session session = factory.openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                Cliente cliente = new Cliente(nombre, email, password, direccion, telefono, estado);
                insertID = session.save(cliente).toString();
                tx.commit();

            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
            return "Agregado" + insertID;

        });  //FIN CLIENTE-------------------------------------------------------------------------------------

        //RESERVA----------------------------------------------------------------------------------------------
        get("/reservar", (req, res) -> {
            res.redirect("reserva.html");
            return null;
        });

//     //hacer reserva   
        post("/reserva", (req, res) -> {
            System.out.println("LLEGO ID CLIENTE: " + req.body());
            String valores[] = req.body().split("&");
            for (String valore : valores) {
                System.out.println("VALORES OBTENIDO:" + valore.split("=")[1]);
            }
            System.out.println("idcliente = " + req.queryParams("idcliente"));
            int IDcliente = Integer.parseInt(req.queryParams("idcliente"));
            String fechallegada = req.queryParams("fechallegada");
            String fechasalida = req.queryParams("fechasalida");
            float costo = Float.parseFloat(req.queryParams("costo"));

            String insertID = "";
            Session session = factory.openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                Reservacion reserva = new Reservacion(IDcliente, fechallegada, fechasalida, costo);
                insertID = session.save(reserva).toString();
                tx.commit();

            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
            return "Reservado" + insertID;
        });// FIN RESERVA-------------------------------------------------------------------------------------

        //panel administrativo--------------------------------------------------------------------------------
        get("/admin", (req, res) -> {
            res.redirect("admin.html");

            return null;
        });

        post("/admin", (req, res) -> {
//
            String nombre = req.queryParams("nombre");
            String telefono = req.queryParams("telefono");
            String direccion = req.queryParams("direccion");

            String insertID = "";
            Session session = factory.openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                Hotel hotel = new Hotel(nombre, telefono, direccion);
                insertID = session.save(hotel).toString();
                tx.commit();

            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
            return "Agregado" + insertID;
        });//fin del panel administrativo----------------------------------------------------------------------

        get("/chat", (req, res) -> {

            res.redirect("/index_chat.html");
            return null;
        });

    }

    //MODULO DEL CHAT------------------------------------------------------------------------------------------
    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(org.eclipse.jetty.websocket.api.Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
//Builds a HTML element with a sender-name, a message, and a timestamp,

    private static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

}
