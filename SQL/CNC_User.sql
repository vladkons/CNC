CREATE USER CNC IDENTIFIED BY replace_password;


GRANT CONNECT, resource TO CNC;

GRANT CREATE SESSION to cnc;

GRANT ANY PRIVILEGES TO cnc;



GRANT UNLIMITED TABLESPACE TO cnc;
 