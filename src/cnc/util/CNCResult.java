package cnc.util;

public class CNCResult {
    public CNCResult() {
        super();
    }

    public CNCResult(String err, Object obj) {
        super();
        this.err = err;
        this.obj = obj;
    }
    private String err;
    private Object obj;


    public void setErr(String err) {
        this.err = err;
    }

    public String getErr() {
        return err;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }
}
