/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.hotelapi;
import static spark.Spark.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.*;
import org.eclipse.jetty.websocket.api.annotations.*;
/**
 *
 * @author marianny
 */
@WebSocket
public class ChatWebSocketHandler {
        private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Main.nextUserNumber++;
        Main.userUsernameMap.put(user, username);
        Main.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Main.userUsernameMap.get(user);
        Main.userUsernameMap.remove(user);
        Main.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Main.broadcastMessage(sender = Main.userUsernameMap.get(user), msg = message);
    }

}
