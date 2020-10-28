package Handlers;

import DAO.AuthDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import Result.ErrorResult;
import Result.Result;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;

public abstract class Handler implements HttpHandler {

    abstract protected Result workWithService(String requestURI, String reqData);
    String getOrPost;
    Boolean authenticate;
    String authToken;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {

            if (exchange.getRequestMethod().toLowerCase().equals(getOrPost)) {
                if (authenticate) {

                    Headers reqHeaders = exchange.getRequestHeaders();

                    if (reqHeaders.containsKey("Authorization")) {

                        authToken = reqHeaders.getFirst("Authorization");

                        Database db = new Database();
                        Connection conn = db.getConnection();

                        if (new AuthDAO(conn).verify(new AuthToken(authToken))) {
                            success = translateJson(exchange);
                        }
                        else {
                            success = false;

                            Result resp = new ErrorResult("Error auth token not verified.");
                            Gson gson = new Gson();

                            String jsonRespStr = gson.toJson(resp);
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                            OutputStream respBody = exchange.getResponseBody();
                            writeString(jsonRespStr, respBody);
                            respBody.close();
                            exchange.getResponseBody().close();
                        }
                        db.closeConnection(true);
                    }

                }
                else {
                    success = translateJson(exchange);
                }

            }
            if (!success) {

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException | DataAccessException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }

    private boolean translateJson(HttpExchange exchange) throws IOException {
        boolean success = false;

        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);

        Gson gson = new Gson();

        Result resp = workWithService(exchange.getRequestURI().toString(), reqData);

        String jsonRespStr = gson.toJson(resp);


        if (resp.isSuccess()) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRespStr, respBody);

            respBody.close();

            success = true;
        }
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream respBody = exchange.getResponseBody();

            writeString(jsonRespStr, respBody);
            respBody.close();
            exchange.getResponseBody().close();
            success = true;
        }
        return success;
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
