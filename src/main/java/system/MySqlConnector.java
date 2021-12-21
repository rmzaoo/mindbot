package system;

import org.apache.log4j.Logger;
import utils.TerminalColors;

import java.sql.*;

public class MySqlConnector {

    private static final org.apache.log4j.Logger log = Logger.getLogger(MySqlConnector.class);
    public Connection conn = null;
    private Statement st = null;
    private ResultSet rs = null;

    public void startConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mindbot?user=root&password=root");
            log.warn(TerminalColors.YELLOW + "[MYSQL] Successful database connection" + TerminalColors.RESET);
            st = conn.createStatement();

//            closeConn();
        } catch (SQLException ex) {
            log.fatal(TerminalColors.RED + "[MYSQL] ERROR CONNECTING TO DB" + TerminalColors.RESET);
            log.fatal(TerminalColors.RED + "[MYSQL] " + ex.getMessage() + TerminalColors.RESET);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void closeConn() {
        try {
            st.close();
            conn.close();
            log.warn(TerminalColors.YELLOW + "[MYSQL] Connection with DB has been closed" + TerminalColors.RESET);
        } catch (SQLException ex) {
            log.fatal(TerminalColors.RED + "[MYSQL] ERROR CLOSING DB" + TerminalColors.RESET);
            log.fatal(TerminalColors.RED + "[MYSQL] " + ex.getMessage() + TerminalColors.RESET);
        }
    }


    public ResultSet executeQuery(String query) {
        try {
            log.warn(TerminalColors.YELLOW + "[MYSQL] Executing query" + TerminalColors.RESET);
            log.warn(TerminalColors.YELLOW + "[MYSQL] " + query + TerminalColors.RESET);
            st = conn.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            log.fatal(TerminalColors.RED + "[MYSQL] ERROR Executing Query" + TerminalColors.RESET);
            log.fatal(TerminalColors.RED + "[MYSQL] " + ex.getMessage() + TerminalColors.RESET);
        }

        return rs;
    }

    public void executeUpdate(String query) {
        try {
            log.warn(TerminalColors.YELLOW + "[MYSQL] Executing query" + TerminalColors.RESET);
            log.warn(TerminalColors.YELLOW + "[MYSQL] " + query + TerminalColors.RESET);
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            log.fatal(TerminalColors.RED + "[MYSQL] ERROR Executing Query" + TerminalColors.RESET);
            log.fatal(TerminalColors.RED + "[MYSQL] " + ex.getMessage() + TerminalColors.RESET);
        }
    }

}
