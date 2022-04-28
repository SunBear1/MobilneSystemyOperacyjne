// IMyAidlInterface.aidl
package pl.edu.pg.lab4;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getMessage();
    void setMessage(String msg);
}