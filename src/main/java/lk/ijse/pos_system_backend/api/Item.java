package lk.ijse.pos_system_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.pos_system_backend.Db.CustomerDb;
import lk.ijse.pos_system_backend.Db.ItemDb;
import lk.ijse.pos_system_backend.dto.CustomerDTO;
import lk.ijse.pos_system_backend.dto.ItemDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "item", urlPatterns = "/item")
public class Item extends HttpServlet {
    Connection connection;
    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/pos");
            this.connection = pool.getConnection();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action.equals("generateItemCode")){
            generateItemCode(req,resp);
        }else if (action.equals("getAllItem")){
            getAllItem(req,resp);
        } else if (action.equals("getItem")) {
            String code = req.getParameter("itemCode");
            getItem(req,resp,code);
        }
    }

    private void generateItemCode(HttpServletRequest req, HttpServletResponse resp){
        ItemDb itemDb = new ItemDb();
        String itemCode = itemDb.generateItemCode(connection);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(itemCode);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")){
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            var itemDb = new ItemDb();
            boolean result = itemDb.saveItem(connection, itemDTO);

            if (result){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Item information saved successfully!");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Failed to saved item information!");
            }
        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")){
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            var itemDb = new ItemDb();
            boolean result = itemDb.updateItem(connection, itemDTO);

            if (result){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Customer information updated successfully!");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Failed to saved customer information!");
            }
        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("itemCode");
        System.out.println(code);
        var itemDb = new ItemDb();
        boolean result = itemDb.deleteItem(connection, code);

        if (result){
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Customer information deleted successfully!");
        }else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Failed to saved customer information!");
        }
    }

    private void getAllItem(HttpServletRequest req, HttpServletResponse resp){
        var itemDb = new ItemDb();
        ArrayList<ItemDTO> allItem = itemDb.getAllItem(connection);

        Jsonb jsonb = JsonbBuilder.create();
        var json = jsonb.toJson(allItem);

        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    private void getItem(HttpServletRequest req, HttpServletResponse resp, String code){
        var itemDb = new ItemDb();
        ItemDTO itemDTO = itemDb.getItem(connection, code);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(itemDTO);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
