BEGIN 
      EXECUTE IMMEDIATE '   TRUNCATE TABLE ATTENDEES';
      
        FOR x IN 1..100 LOOP

            Insert into ATTENDEES (PK,NAME,COMPANY,EMAIL,TWITTER,REQUESTED_COUNT,CREATED_DATE,LAST_REQUESTED_DATE) 
            values (null,'NAME' || x,'COMP1' || x,'EMAIL1' || x,'TWITT1' || x, 0, systimestamp, null);
        END LOOP;
        
        COMMIT;
    END;