package com.example.jerseyDemo.model;

import com.example.jerseyDemo.service.ThreadPoolExecutorService;
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
@Path("/")
public class JacketDemo {
    @Autowired
    private Environment env;
    private static final Logger LOGGER = LoggerFactory.getLogger(JacketDemo.class);

    @GET
    @Path("httpCallout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getHttpCall() {
        String host = env.getProperty("spring.application.name");
        System.out.println("host is: " + host);
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
        String url = env.getProperty("httpCallout");
        LOGGER.info("Url passed is: " + url);
        WebTarget webTarget = client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.get();
        LOGGER.info("Connected to URL : " + url + " status code is: " + response.getStatus());
        return Response.ok("HttpCallout is Done, check console logs").build();

    }

    @GET
    @Path("sqlCallout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getDbCall() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + env.getProperty("jdbc.host") + ":" +
                    env.getProperty("jdbc.port") + "/" +
                    env.getProperty("jdbc.db");
            String user = env.getProperty("jdbc.user");
            String pass = env.getProperty("jdbc.pass");
            Connection con = DriverManager.getConnection(
                    url, user, pass);
            LOGGER.info("Connected to mysql database......" + url + user + pass);
            Statement stmt = con.createStatement();
            String sql = "";
            try {
                sql = "DROP TABLE JERSEYTABLE";
                stmt.execute(sql);
            } catch (Exception e) {

            }
            sql = "CREATE TABLE JERSEYTABLE  (first VARCHAR(255)) ";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO JERSEYTABLE VALUES ('first-name')";
            stmt.executeUpdate(sql);
            sql = "select * from JERSEYTABLE";
            ResultSet rs = stmt.executeQuery(sql);
            LOGGER.info("Statement executed is: " + rs);
            while (rs.next())
                LOGGER.info("Data from Sql DB is : " + rs.getString("first"));
            sql = "DROP TABLE JERSEYTABLE";
            stmt.execute(sql);
            con.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("Error in DB Callout....");
        }

        LOGGER.info("SQL callout done");
        return Response.ok("dbCallout is Done, check console logs").build();

    }

    @GET
    @Path("threadCallout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getThreadCall() {
        ThreadPoolExecutorService.callSimpleThreadPoolExecutorService();
        LOGGER.info("hellloo");
        return Response.ok("threadCallout is Done , Check Console Logs").build();

    }


    @GET
    @Path("mongoCallout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMongoCall() {
        String host = env.getProperty("spring.data.mongodb.host");
        String port = env.getProperty("spring.data.mongodb.port");
        try {
            MongoClient mongo = new MongoClient(host, Integer.parseInt(port));
//            ConnectionString connectionString = new ConnectionString("mongodb+srv://ankurrawat:12345@cluster0.fsybrpg.mongodb.net/TaskManager?retryWrites=true&w=majority");
//            MongoClientSettings settings = MongoClientSettings.builder()
//                    .applyConnectionString(connectionString)
//                    .build();
//            MongoClient mongoClient = MongoClients.create(settings);
            LOGGER.info("Connected to MongoDB server");
            MongoDatabase db = mongo.getDatabase("jersey");
            LOGGER.info("Connected to Database : " + db.getName());
            MongoCollection<Document> table = db.getCollection("demo");
            Document doc = new Document("name", "Peter Parker");
            doc.append("id", 12);
            table.insertOne(doc);
            LOGGER.info("Data Inserted Successfully");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("Error in DB Callout....");
        }
        return Response.ok("MongoCallout is Done , Check Console Logs").build();

    }

}
