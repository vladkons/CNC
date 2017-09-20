SELECT  * FROM ATTENDEES 
WHERE PK  IN (select t2.pk from (   SELECT t1.PK pk, rownum as r 
                                    FROM (  SELECT PK  
                                            FROM ATTENDEES 
                                            ORDER BY CREATED_DATE  DESC) t1
 ) t2
 WHERE t2.r BETWEEN 21 AND 44 ) 
 ORDER BY CREATED_DATE DESC;
 
 
 SELECT PK, NAME, IMAGE FROM ATTENDEES WHERE REPLACE(REPLACE(NAME,CHR(13), ' '),CHR(10),' ')  LIKE '%_2%' ;
 
 SELECT PK, NAME, IMAGE FROM ATTENDEES WHERE NAME  LIKE '%_2%' ;
 
 SELECT PK, NAME, IMAGE FROM ATTENDEES WHERE REPLACE(REPLACE(NAME,CHR(13), ' '),CHR(10),' ')  LIKE '%%' escape '!' ORDER BY CREATED_DATE DESC;
 
 
 truncate table attendees;
 
UPDATE attendees
	SET image_thumb = null
	WHERE pk = 488;


SELECT * FROM 
(   SELECT a.PK, a.NAME, a.IMAGE, a.IMAGE_THUMB, a.IMAGE_MORPH, a.IMAGE_ASSOC, a.CNC_PATH, A.CREATED_DATE, rownum as r 
    FROM ATTENDEES a WHERE upper(REPLACE(REPLACE(A.NAME,CHR(13), ' '),CHR(10),' '))  LIKE '%_%' ESCAPE '!' 
    ORDER BY CREATED_DATE DESC )  
WHERE r < 6 ORDER BY CREATED_DATE DESC