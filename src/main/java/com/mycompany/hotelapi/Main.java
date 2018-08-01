package com.mycompany.hotelapi;

import com.google.gson.Gson;
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

public class Main {

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
//        SessionFactory factory = new Configuration().configure().buildSessionFactory();
//        System.out.println("CFG and HBM loaded");
//        Session session = factory.openSession();
//        session.beginTransaction();
//        System.out.println("transaction began");

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
//
//        //Login
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
            return new Gson().toJson("Usuario registrado");

        });

        post("/cliente", (req, res) -> {
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
            return new Gson().toJson("Agregado");
        });

//        
//     //hacer reserva   
        post("/reserva", (req, res) -> {

            int IDcliente = Integer.parseInt(req.queryParams("IDcliente"));
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
            return new Gson().toJson("Agregado");
        });

    }

}
