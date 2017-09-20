package cnc.jdbc;

import cnc.model.Attendee;

import cnc.util.ExceptionString;

import java.io.StringReader;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.util.ArrayList;

import cnc.util.CNCResult;

import java.sql.Clob;

public class AttendeeDAO {
    public AttendeeDAO() {
        super();
    }

    // Get  attedee(s) by partial name
    public static CNCResult getAttendeeCount() {
       
        String ret = "";
        String count = ""; 
            
        PreparedStatement st = null;
        Connection conn = CNCDataSource.getConnection();
       
        try {
            
            //Get Attendee count
            String sql =
            " SELECT count(*) FROM ATTENDEES";

            st = conn.prepareStatement(sql);
            ResultSet rs  = st.executeQuery();
            while (rs.next()){
                    count = String.valueOf(rs.getBigDecimal(1).intValue());  
            }
        } catch (SQLException e) {
            ret = ExceptionString.toString(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return new CNCResult(ret, count);
    }

    // Get  attedee(s) by partial name
    public static CNCResult getAttendeePaged(int first, int last) {
       
        String ret = "";
        
        ArrayList<Attendee> retList = new ArrayList<Attendee>();
        PreparedStatement st = null;
        Connection conn = CNCDataSource.getConnection();
        
    
        try {
            
            //Select a page of records - page size defined by 'first' and 'last'
            //Note - order by creation_date (the most recent)
            String sql = " SELECT  * FROM ATTENDEES " + 
                         " WHERE PK  IN (select t2.pk from (    SELECT t1.PK pk, rownum as r " +
                         "                                       FROM (  SELECT PK  " +
                         "                                               FROM ATTENDEES  " +
                         "                                               ORDER BY CREATED_DATE  DESC) t1 " +
                         " ) t2 " +
                         " WHERE t2.r BETWEEN ? AND ? ) ORDER BY CREATED_DATE DESC ";
    

            st = conn.prepareStatement(sql);
            st.setInt(1, first);
            st.setInt(2, last);
            ResultSet rs  = st.executeQuery();
            while (rs.next()){
                
                Clob clob = rs.getClob("image"); 
                Clob clobt = rs.getClob("image_thumb"); 
                Clob clobm = rs.getClob("image_morph"); 
                Clob cloba = rs.getClob("image_assoc"); 
                Attendee att = new Attendee(
                                    rs.getBigDecimal("pk"), 
                                    rs.getString("name"),
                                    clob != null ? clob.getSubString(1, (int) clob.length()): "",
                                    clobt != null ? clobt.getSubString(1, (int) clobt.length()): "",
                                    clobm != null ? clobm.getSubString(1, (int) clobm.length()): "",
                                    cloba != null ? cloba.getSubString(1, (int) cloba.length()): "",
                                    rs.getString("cnc_path"));
                retList.add(att);
                
            }
            
            
        } catch (SQLException e) {
            ret = ExceptionString.toString(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return new CNCResult(ret, retList);
    }
    
    
    
    
    // Get  attedee(s) by partial name
    public static CNCResult getAttendeeByID(int id) {
       
        String ret = "";
        

        PreparedStatement st = null;
        Connection conn = CNCDataSource.getConnection();
        Attendee att = null;
    
        try {
            
            //Select a page of records - page size defined by 'first' and 'last'
            //Note - order by creation_date (the most recent)
            String sql = " SELECT  * FROM ATTENDEES " + 
                         " WHERE PK  = ? ";
    

            st = conn.prepareStatement(sql);
            st.setBigDecimal(1, new BigDecimal(id));
           
            ResultSet rs  = st.executeQuery();
            while (rs.next()){
                
                Clob clob = rs.getClob("image"); 
                Clob clobt = rs.getClob("image_thumb"); 
                Clob clobm = rs.getClob("image_morph"); 
                Clob cloba = rs.getClob("image_assoc"); 
                att = new Attendee(
                                    rs.getBigDecimal("pk"), 
                                    rs.getString("name"),
                                    clob != null ? clob.getSubString(1, (int) clob.length()): "",
                                    clobt != null ? clobt.getSubString(1, (int) clobt.length()): "",
                                    clobm != null ? clobm.getSubString(1, (int) clobm.length()): "",
                                    cloba != null ? cloba.getSubString(1, (int) cloba.length()): "",
                                    rs.getString("cnc_path"));
               
                
            }
            
            
        } catch (SQLException e) {
            ret = ExceptionString.toString(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return new CNCResult(ret, att);
    }

    // Get  attedee(s) by partial name
    public static CNCResult getAttendee(String name) {
       
        String ret = "";
        
        ArrayList<Attendee> retList = new ArrayList<Attendee>();
        PreparedStatement st = null;
        Connection conn = CNCDataSource.getConnection();
        
    
        try {
            
            name = name
                .replace("!", "!!")
                .replace("%", "!%")
                .replace(" ", "");
            
            //Strip space(s) from name, also - convert to uppercase, limit result set to 20
            String sql =
            " SELECT * FROM " +
            " (   SELECT a.PK, a.NAME, a.IMAGE, a.IMAGE_THUMB, a.IMAGE_MORPH, a.IMAGE_ASSOC, a.CNC_PATH, A.CREATED_DATE, rownum as r " +
            "    FROM ATTENDEES a WHERE upper(REPLACE(REPLACE(A.NAME,CHR(13), ' '),CHR(10),' '))  LIKE ? ESCAPE '!'  " +
            "    ORDER BY CREATED_DATE DESC )   " +
            " WHERE r < 21 ORDER BY CREATED_DATE DESC " ;

            st = conn.prepareStatement(sql);
            st.setString(1, "%" + name.toUpperCase() + "%"); // convert search string to uppercase
            ResultSet rs  = st.executeQuery();
            while (rs.next()){
                
                Clob clob = rs.getClob("image"); 
                Clob clobt = rs.getClob("image_thumb"); 
                Clob clobm = rs.getClob("image_morph"); 
                Clob cloba = rs.getClob("image_assoc"); 
                
                Attendee att = new Attendee(
                                    rs.getBigDecimal("pk"), 
                                    rs.getString("name"),
                                    clob != null ? clob.getSubString(1, (int) clob.length()): "",
                                    clobt != null ? clobt.getSubString(1, (int) clobt.length()): "",
                                    clobm != null ? clobm.getSubString(1, (int) clobm.length()): "",
                                    cloba != null ? cloba.getSubString(1, (int) cloba.length()): "",
                                    rs.getString("cnc_path"));
                retList.add(att);
                
            }
        } catch (SQLException e) {
            ret = ExceptionString.toString(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return new CNCResult(ret, retList);
    }

    //Create attendee
    public static String insertAttendee(Attendee attendee) {

        String ret = "OK";
        PreparedStatement st = null;
        Connection conn = CNCDataSource.getConnection();
        try {

            String sql =
                "INSERT INTO ATTENDEES (PK,NAME,COMPANY,EMAIL,TWITTER, " +
                "IMAGE, IMAGE_THUMB, IMAGE_MORPH, IMAGE_ASSOC, CNC_PATH, " +
                "REQUESTED_COUNT,CREATED_DATE,LAST_REQUESTED_DATE) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            st = conn.prepareStatement(sql);
            st.setBigDecimal(1, null);
            st.setString(2, attendee.getName());
            st.setString(3, attendee.getCompany());
            st.setString(4, attendee.getEmail());
            st.setString(5, attendee.getTwitter());
            st.setCharacterStream(6, new StringReader(attendee.getImage()), attendee.getImage().length());
            st.setCharacterStream(7, new StringReader(attendee.getImageThumb()), attendee.getImageThumb().length());
            st.setCharacterStream(8, new StringReader(attendee.getImageMorph()), attendee.getImageMorph().length());
            st.setCharacterStream(9, new StringReader(attendee.getImageAssoc()), attendee.getImageAssoc().length());
            st.setString(10, attendee.getCncPath());
            st.setBigDecimal(11, new BigDecimal(0)); // gets overriden by a trigger on insert
            st.setTimestamp(12, new Timestamp(0)); // gets overriden by a trigger on insert
            st.setTimestamp(13, null);

            st.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            ret = ExceptionString.toString(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return ret;
    }


}
